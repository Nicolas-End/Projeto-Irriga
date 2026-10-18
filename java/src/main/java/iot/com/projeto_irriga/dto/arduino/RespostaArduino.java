package iot.com.projeto_irriga.dto.arduino;


import iot.com.projeto_irriga.enums.arduino.TiposComandos;

public record  RespostaArduino <T>(
        TiposComandos tipo, T valor, String comando, boolean ok) {
}
