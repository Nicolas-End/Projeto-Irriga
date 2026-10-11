package iot.com.projeto_irriga.dto.consumo_agua;

import iot.com.projeto_irriga.enums.arduino.ModeloArduino;
import iot.com.projeto_irriga.enums.info_irriga.TipoConfiguacao;

import java.util.Date;

public record ConsumoAguaFormatadoDTO (Double umidade, Double tempoLigado, Double aguaTotal, TipoConfiguacao modo, ModeloArduino dispositivo,
                                       Date data) {
}
