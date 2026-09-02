package iot.com.projeto_irriga.enums.arduino;

public enum StatusResposta {
    OK ("OK"),
    ERROR("ERROR");
    private final String status ;
    StatusResposta(String status){
        this.status = status;
    }
}
