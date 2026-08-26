package iot.com.projeto_irriga.enums.arduino;

import lombok.Getter;

@Getter
public enum TiposComandos {
    EXECUTAR("EXECUTAR"),
    INFORMATIVO("INFORMATIVO");

    private final String tipo ;
    TiposComandos(String tipo){
        this.tipo = tipo;
    }
}
