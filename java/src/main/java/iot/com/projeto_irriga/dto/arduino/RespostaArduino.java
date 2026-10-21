package iot.com.projeto_irriga.dto.arduino;



public record  RespostaArduino <T>(
        String tipo, T valor, String comando, boolean ok) {
}
