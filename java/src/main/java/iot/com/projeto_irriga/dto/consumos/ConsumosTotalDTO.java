package iot.com.projeto_irriga.dto.consumos;

import java.util.Date;

public record ConsumosTotalDTO(Double consumoTotal, Double tempoLigadoTotal, int quantidadeVezesLigada, Date ultimaVezIrrigado, double consumoEnergico) {
}
