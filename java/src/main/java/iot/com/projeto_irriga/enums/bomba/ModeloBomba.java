package iot.com.projeto_irriga.enums.bomba;

public enum ModeloBomba {

    PERISTATICA("PERISTATICA"),
    DESCONHECIDO("DESCONHECIDO"),
    SUBMERSIVEL("SUB");


    private String modelo;

    ModeloBomba(String modelo){
        this.modelo = modelo;
    }
}
