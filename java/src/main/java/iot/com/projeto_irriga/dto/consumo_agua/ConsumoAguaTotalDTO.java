package iot.com.projeto_irriga.dto.consumo_agua;

import java.util.Date;

public record ConsumoAguaTotalDTO(Double consumoTotal, Double tempoLigadoTotal, int quantidadeVezesLigada, Date ultimaVezIrrigado) {
}
