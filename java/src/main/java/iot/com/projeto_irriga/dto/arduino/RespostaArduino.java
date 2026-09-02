package iot.com.projeto_irriga.dto.arduino;

import iot.com.projeto_irriga.enums.arduino.StatusResposta;

public record RespostaArduino(

        StatusResposta status, String message) {
}
