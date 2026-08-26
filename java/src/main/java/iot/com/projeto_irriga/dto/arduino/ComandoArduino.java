package iot.com.projeto_irriga.dto.arduino;

import iot.com.projeto_irriga.enums.arduino.TiposComandos;

public record ComandoArduino(TiposComandos tipo, String comando) {
}
