package iot.com.projeto_irriga.dto.arduino;

import com.fasterxml.jackson.annotation.JsonProperty;
import iot.com.projeto_irriga.enums.arduino.StatusResposta;

public record RespostaArduino(
        @JsonProperty("StatusResposta")
        Boolean status, String message) {
}
