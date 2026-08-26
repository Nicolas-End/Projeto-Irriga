package iot.com.projeto_irriga.domains.arduino;

import iot.com.projeto_irriga.dto.arduino.ComandoArduino;
import iot.com.projeto_irriga.dto.arduino.RespostaArduino;
import iot.com.projeto_irriga.enums.arduino.TiposComandos;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/arduino")
public class ArduinoController {

    private final ArduinoService servico;

    public ArduinoController(ArduinoService servico) {
        this.servico = servico;
    }

    @PostMapping("/ligar-led")
    public ResponseEntity<RespostaArduino> ligarLed() {
        return executar(new ComandoArduino(TiposComandos.EXECUTAR, ""));
    }

    @PostMapping("/desligar-led")
    public ResponseEntity<RespostaArduino> desligarLed() {
        return executar(new ComandoArduino(TiposComandos.EXECUTAR, ""));
    }

    @GetMapping("/sensor")
    public ResponseEntity<RespostaArduino> lerSensor() {
        return executar(new ComandoArduino(TiposComandos.EXECUTAR, ""));
    }

    private ResponseEntity<RespostaArduino> executar(ComandoArduino comando) {
        try {
            RespostaArduino resposta = servico.enviarEEsperarResposta(comando, 3000);
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new RespostaArduino("erro"));
        }
    }
}
