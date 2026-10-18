package iot.com.projeto_irriga.infra.utils.bluetooth;


import iot.com.projeto_irriga.domains.info_irriga.InfoIrrigaEntity;
import iot.com.projeto_irriga.domains.info_irriga.InfoIrrigaService;
import iot.com.projeto_irriga.dto.arduino.ComandoArduino;
import iot.com.projeto_irriga.dto.arduino.ConfiguracaoArduinoDTO;
import iot.com.projeto_irriga.dto.arduino.RespostaArduino;
import iot.com.projeto_irriga.enums.arduino.TiposComandos;
import iot.com.projeto_irriga.infra.utils.model.response.ApiResponse;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class BluetoothSerivce {
    private final BluetoothUtil bluetoothUtil;
    private final InfoIrrigaService infoIrrigaService;

    public BluetoothSerivce (BluetoothUtil bluetoothUtil, InfoIrrigaService infoIrrigaService){
        this.bluetoothUtil = bluetoothUtil;
        this.infoIrrigaService = infoIrrigaService;
    }

    public RespostaArduino pingTeste(){

        return this.enviarComandoArduino("ping", null);
    }


    @PostConstruct
    public RespostaArduino configurarArduino(){
        log.info("Configurando Arduino");
        
        ApiResponse databaseIrrigaDatas = this.infoIrrigaService.pegarDadosInfoIrriga();

        if(!databaseIrrigaDatas.getSucess()){
            return  null;
        }

        ConfiguracaoArduinoDTO configArduino = this.converterInfoIrrigaDatasEmConfiguracaoArduino((InfoIrrigaEntity) databaseIrrigaDatas.getDatas());

        return this.enviarComandoArduino("configurar", configArduino);



    }

    public RespostaArduino solicitarUmidadeSoloArduino(){

        return this.enviarComandoArduino("ler_sensor",null);

    }

    // padronização de comando ao arduino
    private <T> RespostaArduino enviarComandoArduino (String comando, T valor){
        try {

            ComandoArduino comandoArduino = new ComandoArduino(TiposComandos.COMANDO, comando, valor);
            return this.bluetoothUtil.enviarEEsperarResposta(comandoArduino, 3000);

        }catch (Exception e){
            log.error("Erro ao comunicar com Arduino ");
            log.error(e.getMessage());
            return null;

        }
    }

    private ConfiguracaoArduinoDTO converterInfoIrrigaDatasEmConfiguracaoArduino(InfoIrrigaEntity entity){
        return ConfiguracaoArduinoDTO.builder()
                .tempoRega((long)entity.getDuracaoIrrigacao())
                .tempoEspera((long) entity.getIntervalo())
                .umidadeMinima((long) entity.getUmidadeMinima())
                .umidadeMaxima((long) entity.getUmidadeMinima() + 20)
                .build();

    }

}