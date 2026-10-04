package iot.com.projeto_irriga.enums.info_irriga;

public enum TipoConfiguacao {

    ECONOMICA("ECONOMICA"),
    EQUILIBRADA("EQUILIBRADA"),
    CUSTOMIZADO("CUSTOMIZADO");


    private String tipo;

    TipoConfiguacao(String tipo){
        this.tipo = tipo;
    }
}
