package iot.com.projeto_irriga.dto.info_irriga;

import lombok.Setter;


public record InfoIrrigaDatasDTO(String arduino, String emailUsuario, double intervaloIrrigacao, double umidadeMinima, double duracaoIrrigacao) {

}
