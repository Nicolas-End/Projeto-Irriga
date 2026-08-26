package iot.com.projeto_irriga.domains.arduino;

import com.fazecast.jSerialComm.SerialPort;
import iot.com.projeto_irriga.dto.arduino.ComandoArduino;
import iot.com.projeto_irriga.dto.arduino.RespostaArduino;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ArduinoService  {

    @Value("${arduino.porta}")
    private String nomePorta;

    @Value("${arduino.baudrate}")
    private int baudRate;

    private SerialPort porta;
    private final ObjectMapper mapper = new ObjectMapper();

    // Fila que recebe as linhas lidas da serial, uma por vez
    private final BlockingQueue<String> filaRespostas = new LinkedBlockingQueue<>();

    // Garante que só um comando por vez seja enviado (evita misturar respostas)
    private final ReentrantLock lock = new ReentrantLock();

    @PostConstruct
    public void iniciar() {
        porta = SerialPort.getCommPort(nomePorta);
        porta.setBaudRate(baudRate);
        porta.setNumDataBits(8);
        porta.setNumStopBits(SerialPort.ONE_STOP_BIT);
        porta.setParity(SerialPort.NO_PARITY);
        porta.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);

        if (!porta.openPort()) {
            throw new IllegalStateException("Não foi possível abrir a porta " + nomePorta);
        }

        // Thread dedicada, sempre lendo a serial em segundo plano
        Thread threadLeitura = new Thread(this::lerContinuamente);
        threadLeitura.setDaemon(true);
        threadLeitura.start();

        System.out.println("[Arduino] Conectado em " + nomePorta);
    }

    private void lerContinuamente() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(porta.getInputStream()))) {

            String linha;
            while ((linha = reader.readLine()) != null) {
                if (!linha.isBlank()) {
                    filaRespostas.offer(linha.trim());
                }
            }
        } catch (Exception e) {
            System.out.println("[Arduino] Erro na leitura contínua: " + e.getMessage());
        }
    }

    // Método chamado pelo Controller: envia comando e ESPERA a resposta
    public RespostaArduino enviarEEsperarResposta(ComandoArduino comando, long timeoutMs) throws Exception {
        lock.lock(); // só um comando em voo por vez
        try {
            filaRespostas.clear(); // descarta qualquer lixo pendente na fila

            String json = mapper.writeValueAsString(comando);
            OutputStream saida = porta.getOutputStream();
            saida.write((json + "\n").getBytes());
            saida.flush();

            // Espera até timeoutMs por uma resposta na fila
            String respostaJson = filaRespostas.poll(timeoutMs, TimeUnit.MILLISECONDS);

            if (respostaJson == null) {
                throw new RuntimeException("Timeout: Arduino não respondeu a tempo");
            }

            return mapper.readValue(respostaJson, RespostaArduino.class);
        } finally {
            lock.unlock();
        }
    }

    @PreDestroy
    public void encerrar() {
        if (porta != null && porta.isOpen()) {
            porta.closePort();
        }
    }
}
