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

public class ArduinoBluetooth {



    @Value("${ble.device-address}")
    private String deviceAddress;

    @Value("${ble.adapter:hci0}")
    private String adapter;

    @Value("${ble.service-uuid}")
    private String serviceUuid;

    @Value("${ble.characteristic-uuid}")
    private String characteristicUuid;

    @Value("${ble.scan-timeout-ms:10000}")
    private long scanTimeoutMs;

    @Value("${ble.response-timeout-ms:5000}")
    private long responseTimeoutMs;


    private DeviceManager deviceManager;

    private volatile BluetoothDevice device;

    private volatile BluetoothGattCharacteristic characteristic;


    private final BlockingQueue<String> filaRespostas =
            new LinkedBlockingQueue<>();

    private final ReentrantLock lock =
            new ReentrantLock();


    private final ObjectMapper mapper =
            new ObjectMapper();


    @PostConstruct
    public void iniciar() throws Exception {

        System.out.println("[Arduino BLE] Inicializando...");

        deviceManager = DeviceManager.createInstance(false);

        conectar();

        System.out.println(
                "[Arduino BLE] Conectado ao dispositivo "
                        + deviceAddress
        );
    }


    private void conectar() throws Exception {

        System.out.println(
                "[Arduino BLE] Procurando dispositivo: "
                        + deviceAddress
        );

        device = localizarDispositivo();

        if (device == null) {

            throw new IllegalStateException(
                    "Dispositivo BLE não encontrado: "
                            + deviceAddress
            );
        }

        System.out.println(
                "[Arduino BLE] Dispositivo encontrado: "
                        + device.getAddress()
        );


        if (!device.isConnected()) {

            System.out.println(
                    "[Arduino BLE] Conectando..."
            );

            device.connect();
        }

        System.out.println(
                "[Arduino BLE] Conexão estabelecida."
        );


        BluetoothGattService service =
                aguardarServico(
                        device,
                        serviceUuid,
                        5000
                );

        if (service == null) {

            throw new IllegalStateException(
                    "Serviço GATT não encontrado: "
                            + serviceUuid
            );
        }

        System.out.println(
                "[Arduino BLE] Serviço encontrado: "
                        + service.getUuid()
        );

        characteristic = localizarCaracteristica(
                        service,
                        characteristicUuid
                );

        if (characteristic == null) {

            throw new IllegalStateException(
                    "Característica GATT não encontrada: "
                            + characteristicUuid
            );
        }

        System.out.println(
                "[Arduino BLE] Característica encontrada: "
                        + characteristic.getUuid()
        );


        registrarNotificacoes();

        characteristic.startNotify();

        System.out.println(
                "[Arduino BLE] Notificações ativadas."
        );
    }

    private BluetoothDevice localizarDispositivo() {


        List<BluetoothDevice> conhecidos =
                deviceManager.getDevices();

        BluetoothDevice existente =
                conhecidos.stream()
                        .filter(d ->
                                deviceAddress.equalsIgnoreCase(
                                        d.getAddress()
                                )
                        )
                        .findFirst()
                        .orElse(null);

        if (existente != null) {

            System.out.println(
                    "[Arduino BLE] Dispositivo encontrado "
                            + "entre os dispositivos conhecidos."
            );

            return existente;
        }

        System.out.println(
                "[Arduino BLE] Dispositivo não encontrado "
                        + "na lista conhecida. Iniciando scan..."
        );

        List<BluetoothDevice> escaneados =
                deviceManager.scanForBluetoothDevices(
                        adapter,
                        (int) scanTimeoutMs
                );


        // --------------------------------------------------------
        // 3. Procura pelo MAC
        // --------------------------------------------------------

        return escaneados.stream()
                .filter(d ->
                        deviceAddress.equalsIgnoreCase(
                                d.getAddress()
                        )
                )
                .findFirst()
                .orElse(null);
    }


    // ============================================================
    // AGUARDAR SERVIÇO GATT
    // ============================================================

    private BluetoothGattService aguardarServico(
            BluetoothDevice device,
            String uuid,
            long timeoutMs
    ) throws InterruptedException {

        long limite =
                System.currentTimeMillis() + timeoutMs;

        BluetoothGattService service =
                buscarServico(device, uuid);

        while (
                service == null
                        &&
                        System.currentTimeMillis() < limite
        ) {

            Thread.sleep(300);

            service =
                    buscarServico(
                            device,
                            uuid
                    );
        }

        return service;
    }


    // ============================================================
    // BUSCAR SERVIÇO
    // ============================================================

    private BluetoothGattService buscarServico(
            BluetoothDevice device,
            String uuid
    ) {

        List<BluetoothGattService> services =
                device.getGattServices();

        if (services == null) {
            return null;
        }

        return services.stream()
                .filter(service ->
                        uuid.equalsIgnoreCase(
                                service.getUuid()
                        )
                )
                .findFirst()
                .orElse(null);
    }


    // ============================================================
    // BUSCAR CARACTERÍSTICA
    // ============================================================

    private BluetoothGattCharacteristic localizarCaracteristica(
            BluetoothGattService service,
            String uuid
    ) {

        List<BluetoothGattCharacteristic> characteristics =
                service.getGattCharacteristics();

        if (characteristics == null) {
            return null;
        }

        return characteristics.stream()
                .filter(characteristic ->
                        uuid.equalsIgnoreCase(
                                characteristic.getUuid()
                        )
                )
                .findFirst()
                .orElse(null);
    }


    // ============================================================
    // NOTIFICAÇÕES
    // ============================================================

    private void registrarNotificacoes()
            throws org.freedesktop.dbus.exceptions.DBusException {

        deviceManager
                .getDbusConnection()
                .addSigHandler(
                        Properties.PropertiesChanged.class,
                        sinal -> {

                            if (characteristic == null) {
                                return;
                            }

                            if (
                                    !"org.bluez.GattCharacteristic1"
                                            .equals(
                                                    sinal.getInterfaceName()
                                            )
                            ) {
                                return;
                            }

                            if (
                                    !sinal.getPath()
                                            .equals(
                                                    characteristic
                                                            .getDbusPath()
                                            )
                            ) {
                                return;
                            }


                            Variant<?> valor =
                                    sinal.getPropertiesChanged()
                                            .get("Value");

                            if (valor == null) {
                                return;
                            }


                            Object valorRecebido =
                                    valor.getValue();

                            if (
                                    !(valorRecebido instanceof byte[] bytes)
                            ) {
                                return;
                            }


                            String mensagem =
                                    new String(
                                            bytes,
                                            StandardCharsets.UTF_8
                                    ).trim();


                            if (mensagem.isBlank()) {
                                return;
                            }


                            System.out.println(
                                    "[Arduino BLE] <- "
                                            + mensagem
                            );


                            filaRespostas.offer(
                                    mensagem
                            );
                        }
                );
    }


    // ============================================================
    // ENVIAR COMANDO
    // ============================================================

    public RespostaArduino enviarEEsperarResposta(
            ComandoArduino comando
    ) throws Exception {

        return enviarEEsperarResposta(
                comando,
                responseTimeoutMs
        );
    }


    // ============================================================
    // ENVIAR E ESPERAR
    // ============================================================

    public RespostaArduino enviarEEsperarResposta(
            ComandoArduino comando,
            long timeoutMs
    ) throws Exception {

        lock.lock();

        try {

            garantirConectado();

            filaRespostas.clear();


            // ----------------------------------------------------
            // Transformar objeto em JSON
            // ----------------------------------------------------

            String json =
                    mapper.writeValueAsString(
                            comando
                    );


            System.out.println(
                    "[Arduino BLE] -> "
                            + json
            );


            // ----------------------------------------------------
            // Enviar JSON para Arduino
            // ----------------------------------------------------

            byte[] dados =
                    json.getBytes(
                            StandardCharsets.UTF_8
                    );


            characteristic.writeValue(
                    dados,
                    Collections.emptyMap()
            );


            // ----------------------------------------------------
            // Esperar resposta
            // ----------------------------------------------------

            String respostaJson =
                    filaRespostas.poll(
                            timeoutMs,
                            TimeUnit.MILLISECONDS
                    );


            if (respostaJson == null) {

                throw new RuntimeException(
                        "Timeout: Arduino não respondeu em "
                                + timeoutMs
                                + " ms"
                );
            }


            // ----------------------------------------------------
            // Converter JSON para DTO
            // ----------------------------------------------------

            return mapper.readValue(
                    respostaJson,
                    RespostaArduino.class
            );

        } finally {

            lock.unlock();
        }
    }


    // ============================================================
    // GARANTIR CONEXÃO
    // ============================================================

    private void garantirConectado()
            throws Exception {

        if (
                device == null
                        ||
                        !device.isConnected()
                        ||
                        characteristic == null
        ) {

            System.out.println(
                    "[Arduino BLE] Conexão perdida. "
                            + "Reconectando..."
            );

            conectar();
        }
    }


    // ============================================================
    // DESCONECTAR
    // ============================================================

    @PreDestroy
    public void encerrar() {

        try {

            if (
                    device != null
                            &&
                            device.isConnected()
            ) {

                System.out.println(
                        "[Arduino BLE] Desconectando..."
                );

                device.disconnect();
            }

        } catch (Exception e) {

            System.out.println(
                    "[Arduino BLE] Erro ao desconectar: "
                            + e.getMessage()
            );
        }
    }
}