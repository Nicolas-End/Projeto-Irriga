package iot.com.projeto_irriga.dto.arduino;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ConfiguracaoArduinoDTO {

    @JsonProperty("tempo_rega")       private final Long tempoRega;
    @JsonProperty("tempo_espera")     private final Long tempoEspera;
    @JsonProperty("tempo_max_manual") private final Long tempoMaxManual;
    @JsonProperty("umidade_minima")   private final Long umidadeMinima;
    @JsonProperty("umidade_maxima")   private final Long umidadeMaxima;

    private ConfiguracaoArduinoDTO(Builder b) {
        this.tempoRega      = b.tempoRega;
        this.tempoEspera    = b.tempoEspera;
        this.tempoMaxManual = b.tempoMaxManual;
        this.umidadeMinima  = b.umidadeMinima;
        this.umidadeMaxima  = b.umidadeMaxima;
    }



    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long tempoRega;
        private Long tempoEspera;
        private Long tempoMaxManual;
        private Long umidadeMinima;
        private Long umidadeMaxima;

        public Builder tempoRega(Long tempoRega) {
            this.tempoRega = tempoRega;
            return this;
        }

        public Builder tempoEspera(Long tempoEspera) {
            this.tempoEspera = tempoEspera;
            return this;
        }

        public Builder tempoMaxManual(Long tempoMaxManual) {
            this.tempoMaxManual = tempoMaxManual;
            return this;
        }

        public Builder umidadeMinima(Long umidadeMinima) {
            this.umidadeMinima = umidadeMinima;
            return this;
        }

        public Builder umidadeMaxima(Long umidadeMaxima) {
            this.umidadeMaxima = umidadeMaxima;
            return this;
        }

        public ConfiguracaoArduinoDTO build() {
            return new ConfiguracaoArduinoDTO(this);
        }
    }
}