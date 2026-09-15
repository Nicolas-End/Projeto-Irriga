package iot.com.projeto_irriga.enums.arduino;

public enum ModeloArduino {

    UNO("UNO"),
    MEGA("MEGA"),
    NANO("NANO"),
    ESP32("ESP32"),
    DUE("DUE");

    private String modelo;

    ModeloArduino(String modelo){
        this.modelo = modelo;
    }
}
