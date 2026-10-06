package iot.com.projeto_irriga.enums.info_irriga;

public enum TipoConfiguacao {

    ECONOMICA("ECONOMICA"),
    EQUILIBRADA("EQUILIBRADA"),
    CUSTOMIZADA("CUSTOMIZADA");


    private String tipo;

    TipoConfiguacao(String tipo){
        this.tipo = tipo;
    }
}
