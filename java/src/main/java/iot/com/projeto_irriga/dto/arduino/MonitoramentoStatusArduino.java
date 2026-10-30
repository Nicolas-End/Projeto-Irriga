package iot.com.projeto_irriga.dto.arduino;

import iot.com.projeto_irriga.dto.consumos.ConsumosTotalDTO;

public record MonitoramentoStatusArduino(ConsumosTotalDTO consumoAgua, int umidadeSolo) {
}
