package iot.com.projeto_irriga.enums.arduino;

import lombok.Getter;

@Getter
public enum TiposComandos {
    comando("comando"),
    resposta("resposta");
    private final String tipo ;
    TiposComandos(String tipo){
        this.tipo = tipo;
    }
}
