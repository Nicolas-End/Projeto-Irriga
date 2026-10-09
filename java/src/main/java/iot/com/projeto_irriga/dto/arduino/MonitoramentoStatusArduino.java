package iot.com.projeto_irriga.dto.arduino;

import iot.com.projeto_irriga.dto.consumo_agua.ConsumoAguaTotalDTO;

public record MonitoramentoStatusArduino(ConsumoAguaTotalDTO consumoAgua, Double umidadeSolo) {
}
