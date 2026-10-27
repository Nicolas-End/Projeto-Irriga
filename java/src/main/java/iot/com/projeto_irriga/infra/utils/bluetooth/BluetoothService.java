package iot.com.projeto_irriga.infra.utils.bluetooth;


import iot.com.projeto_irriga.domains.info_irriga.InfoIrrigaEntity;
import iot.com.projeto_irriga.domains.info_irriga.InfoIrrigaService;
import iot.com.projeto_irriga.dto.arduino.ComandoArduino;
import iot.com.projeto_irriga.dto.arduino.ConfiguracaoArduinoDTO;
import iot.com.projeto_irriga.dto.arduino.RespostaArduino;
import iot.com.projeto_irriga.enums.arduino.TiposComandos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class BluetoothService {
    private final BluetoothUtil bluetoothUtil;
    private final InfoIrrigaService infoIrrigaService;

    public BluetoothService(BluetoothUtil bluetoothUtil, InfoIrrigaService infoIrrigaService){
        this.bluetoothUtil = bluetoothUtil;
        this.infoIrrigaService = infoIrrigaService;
    }

    public RespostaArduino pingTeste(){

        RespostaArduino teste = this.enviarComandoArduino("ping", null);


        return  teste;
    }


    public RespostaArduino configurarArduino(){
        log.info("Configurando Arduino");
        
        InfoIrrigaEntity databaseIrrigaDatas = this.infoIrrigaService.getFirstInfoIrrigaEntity();


        ConfiguracaoArduinoDTO configArduino = this.converterInfoIrrigaDatasEmConfiguracaoArduino(databaseIrrigaDatas);

        return this.enviarComandoArduino("configurar", configArduino);


    }

    public RespostaArduino configurarArduinoComEntidade(InfoIrrigaEntity infoIrrigaEntity){
        log.info("Configurando Arduino");

        ConfiguracaoArduinoDTO configArduino = this.converterInfoIrrigaDatasEmConfiguracaoArduino(infoIrrigaEntity);

        return this.enviarComandoArduino("configurar",configArduino);
    }

    public RespostaArduino solicitarUmidadeSoloArduino(){

        return this.enviarComandoArduino("ler_sensor",null);

    }

    // padronização de comando ao arduino
    private <T> RespostaArduino enviarComandoArduino (String comando, T valor){
        try {

            ComandoArduino comandoArduino = new ComandoArduino(TiposComandos.comando, comando, valor);
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