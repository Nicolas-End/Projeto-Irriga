package iot.com.projeto_irriga.dto.info_irriga;

import iot.com.projeto_irriga.enums.info_irriga.TipoConfiguacao;
import jakarta.annotation.Nullable;
import lombok.Setter;

import java.util.UUID;


public record InfoIrrigaDatasDTO(
        UUID id,
        String arduino,
        String usuarioEmail,
        TipoConfiguacao configuacao,
        @Nullable Double intervaloIrrigacao,
        @Nullable Double umidadeMinima,
        @Nullable Double duracaoIrrigacao
) {
}