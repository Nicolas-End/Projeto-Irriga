package iot.com.projeto_irriga.dto.arduino;

import iot.com.projeto_irriga.enums.arduino.TiposComandos;

public record ComandoArduino<T> (TiposComandos tipo, String comando, T valor) {
}
