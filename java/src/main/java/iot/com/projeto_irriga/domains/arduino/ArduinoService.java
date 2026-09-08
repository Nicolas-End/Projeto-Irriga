package iot.com.projeto_irriga.domains.arduino;

import com.github.hypfvieh.bluetooth.DeviceManager;
import com.github.hypfvieh.bluetooth.wrapper.BluetoothDevice;
import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattCharacteristic;
import com.github.hypfvieh.bluetooth.wrapper.BluetoothGattService;
import iot.com.projeto_irriga.dto.arduino.ComandoArduino;
import iot.com.projeto_irriga.dto.arduino.RespostaArduino;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.freedesktop.dbus.interfaces.Properties;
import org.freedesktop.dbus.types.Variant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ArduinoService {

    @Value("${ble.device-name}")
    private String nomeDispositivo;

    @Value("${ble.adapter:hci0}")
    private String adapter;

    @Value("${ble.service-uuid}")
    private String serviceUuid;

    @Value("${ble.tx-characteristic-uuid}")
    private String txUuid;

    @Value("${ble.rx-characteristic-uuid}")
    private String rxUuid;

    @Value("${ble.scan-timeout-ms:10000}")
    private long scanTimeoutMs;

    private final ObjectMapper mapper = new ObjectMapper();

    private DeviceManager deviceManager;
    private volatile BluetoothDevice device;
    private volatile BluetoothGattCharacteristic txCharacteristic; // notify: Arduino -> Java
    private volatile BluetoothGattCharacteristic rxCharacteristic; // write:  Java -> Arduino

    // Fila que recebe as notificações da characteristic TX, uma por vez
    private final BlockingQueue<String> filaRespostas = new LinkedBlockingQueue<>();

    // Garante que só um comando por vez esteja "em voo" (evita misturar respostas)
    private final ReentrantLock lock = new ReentrantLock();

    @PostConstruct
    public void iniciar() throws Exception {
        deviceManager = DeviceManager.createInstance(false); // system bus, onde o BlueZ roda
        conectar();
        System.out.println("[Arduino BLE] Conectado a " + nomeDispositivo);
    }

    private void conectar() throws Exception {
        device = localizarDispositivo();
        if (device == null) {
            throw new IllegalStateException("Dispositivo BLE '" + nomeDispositivo + "' não encontrado.");
        }

        if (!device.isConnected()) {
            device.connect();
        }

        BluetoothGattService servico = aguardarServico(device, serviceUuid, 5000);
        if (servico == null) {
            throw new IllegalStateException("Serviço GATT " + serviceUuid + " não encontrado.");
        }

        txCharacteristic = localizarCaracteristica(servico, txUuid);
        rxCharacteristic = localizarCaracteristica(servico, rxUuid);

        if (txCharacteristic == null || rxCharacteristic == null) {
            throw new IllegalStateException("Características TX/RX não encontradas no serviço GATT.");
        }

        registrarNotificacoes();
        txCharacteristic.startNotify();
    }

    private BluetoothDevice localizarDispositivo() {
        List<BluetoothDevice> conhecidos = deviceManager.getDevices();
        BluetoothDevice existente = conhecidos.stream()
                .filter(d -> nomeDispositivo.equals(d.getName()))
                .findFirst()
                .orElse(null);
        if (existente != null) {
            return existente;
        }

        List<BluetoothDevice> escaneados = deviceManager.scanForBluetoothDevices(adapter, (int) scanTimeoutMs);
        return escaneados.stream()
                .filter(d -> nomeDispositivo.equals(d.getName()))
                .findFirst()
                .orElse(null);
    }

    private BluetoothGattService aguardarServico(BluetoothDevice dev, String uuid, long timeoutMs) throws InterruptedException {
        long limite = System.currentTimeMillis() + timeoutMs;
        BluetoothGattService servico = buscarServico(dev, uuid);
        while (servico == null && System.currentTimeMillis() < limite) {
            Thread.sleep(300);
            servico = buscarServico(dev, uuid);
        }
        return servico;
    }

    private BluetoothGattService buscarServico(BluetoothDevice dev, String uuid) {
        List<BluetoothGattService> servicos = dev.getGattServices();
        if (servicos == null) return null;
        return servicos.stream()
                .filter(s -> uuid.equalsIgnoreCase(s.getUuid()))
                .findFirst()
                .orElse(null);
    }

    private BluetoothGattCharacteristic localizarCaracteristica(BluetoothGattService servico, String uuid) {
        List<BluetoothGattCharacteristic> caracteristicas = servico.getGattCharacteristics();
        if (caracteristicas == null) return null;
        return caracteristicas.stream()
                .filter(c -> uuid.equalsIgnoreCase(c.getUuid()))
                .findFirst()
                .orElse(null);
    }

    // Substitui a "thread lendo BufferedReader" do serial: aqui a notificação
    // chega de forma assíncrona via sinal D-Bus, então empurramos pra fila.
    private void registrarNotificacoes() throws org.freedesktop.dbus.exceptions.DBusException {
        deviceManager.getDbusConnection().addSigHandler(
                Properties.PropertiesChanged.class,
                (Properties.PropertiesChanged sinal) -> {
                    if (txCharacteristic == null) return;
                    if (!"org.bluez.GattCharacteristic1".equals(sinal.getInterfaceName())) return;
                    if (!sinal.getPath().equals(txCharacteristic.getDbusPath())) return;

                    Variant<?> valor = sinal.getPropertiesChanged().get("Value");
                    if (valor != null && valor.getValue() instanceof byte[] bytes) {
                        String linha = new String(bytes, StandardCharsets.UTF_8).trim();
                        if (!linha.isBlank()) {
                            filaRespostas.offer(linha);
                        }
                    }
                }
        );
    }

    // Mesmo contrato de antes: envia o comando e ESPERA a resposta
    public RespostaArduino enviarEEsperarResposta(ComandoArduino comando, long timeoutMs) throws Exception {
        lock.lock();
        try {
            garantirConectado();
            filaRespostas.clear(); // descarta qualquer notificação pendente

            String json = mapper.writeValueAsString(comando);
            rxCharacteristic.writeValue(json.getBytes(StandardCharsets.UTF_8), Collections.emptyMap());

            String respostaJson = filaRespostas.poll(timeoutMs, TimeUnit.MILLISECONDS);
            if (respostaJson == null) {
                throw new RuntimeException("Timeout: Arduino não respondeu a tempo");
            }

            return mapper.readValue(respostaJson, RespostaArduino.class);
        } finally {
            lock.unlock();
        }
    }

    private void garantirConectado() throws Exception {
        if (device == null || !device.isConnected() || txCharacteristic == null || rxCharacteristic == null) {
            conectar();
        }
    }

    @PreDestroy
    public void encerrar() {
        try {
            if (device != null && device.isConnected()) {
                device.disconnect();
            }
        } catch (Exception e) {
            System.out.println("[Arduino BLE] Erro ao desconectar: " + e.getMessage());
        }
    }
}