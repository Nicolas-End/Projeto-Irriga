package iot.com.projeto_irriga.domains.info_irriga;

import iot.com.projeto_irriga.dto.arduino.RespostaArduino;
import iot.com.projeto_irriga.dto.info_irriga.InfoIrrigaDatasDTO;
import iot.com.projeto_irriga.infra.utils.bluetooth.BluetoothService;
import iot.com.projeto_irriga.infra.utils.model.response.ApiResponse;
import iot.com.projeto_irriga.infra.utils.model.response.ResponseUtil;
import iot.com.projeto_irriga.infra.utils.scheduler.ArduinoSchedulers;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class InfoIrrigaService {

    private final InfoIrrigaRepository infoIrrigaRepository;
    private final ResponseUtil responseUtil;
    private final ArduinoSchedulers arduinoSchedulers;
    private final BluetoothService ble;

    public InfoIrrigaService (BluetoothService ble  , InfoIrrigaRepository infoIrrigaRepository, ResponseUtil responseUtil, ArduinoSchedulers arduinoSchedulers){
        this.infoIrrigaRepository = infoIrrigaRepository;
        this.responseUtil = responseUtil;
        this.arduinoSchedulers = arduinoSchedulers;
        this.ble = ble;
    }


    @PostConstruct
    public void configurarSchedulerIrrigacao(){

        InfoIrrigaEntity entity = this.getFirstInfoIrrigaEntity();

        ArduinoSchedulers.SET_IRRIGATION_SCHEDULE((long) entity.getIntervalo() + 10, TimeUnit.SECONDS);

        return;
    }


    public ApiResponse pegarDadosInfoIrriga(){

        InfoIrrigaEntity infoIrrigaEntity = this.getFirstInfoIrrigaEntity();
        if(infoIrrigaEntity == null){

            return  this.responseUtil.error(null, "informações Não cadastradas", HttpStatus.NOT_FOUND);
        }

        InfoIrrigaDatasDTO infoIrrigaDatasDTO = new InfoIrrigaDatasDTO(infoIrrigaEntity.getId() ,infoIrrigaEntity.getArduino().getArduinoModel().toString(), infoIrrigaEntity.getUsuario().getEmail(), infoIrrigaEntity.getTipoConfiguacao(),infoIrrigaEntity.getIntervalo(), infoIrrigaEntity.getUmidadeMinima(), infoIrrigaEntity.getDuracaoIrrigacao()) ;


        return  this.responseUtil.sucess(infoIrrigaDatasDTO, "informações Verificadas", HttpStatus.OK);

    }


    public ApiResponse reconfigurarDadosInfoIrriga(InfoIrrigaDatasDTO dto) {

        Optional<InfoIrrigaEntity> optional =
                this.infoIrrigaRepository.findById(dto.id());

        if (optional.isEmpty()) {
            return this.responseUtil.error(
                    null,
                    "Informações da irrigação não encontradas",
                    HttpStatus.NOT_FOUND
            );
        }

        InfoIrrigaEntity entity = optional.get();


        if (entity.getTipoConfiguacao() != dto.configuacao()){
            entity.setTipoConfiguacao(dto.configuacao());
        }

        if (dto.intervaloIrrigacao() != null) {

            if (dto.intervaloIrrigacao() > 0) {
                entity.setIntervalo(
                        dto.intervaloIrrigacao()
                );

                ArduinoSchedulers.STOP_IRRIGATION_SCHEDULE();

                ArduinoSchedulers.SET_IRRIGATION_SCHEDULE((long) entity.getIntervalo(),TimeUnit.SECONDS);

                entity.setIrrigaPorIntervalo(true);

            } else {
                entity.setIrrigarPorUmidade(false);
                entity.setIrrigaPorIntervalo(false);
            }

        }


        if (dto.duracaoIrrigacao() != null
                && dto.duracaoIrrigacao() > 0) {

            entity.setDuracaoIrrigacao(
                    dto.duracaoIrrigacao()
            );


        }


        if (dto.umidadeMinima() != null
                && dto.umidadeMinima() >= 0) {

            entity.setUmidadeMinima(
                    dto.umidadeMinima()
            );
            entity.setIrrigarPorUmidade(true);
        }else {
            entity.setUmidadeMinima(0);
            entity.setIrrigaPorIntervalo(false);
        }

        InfoIrrigaEntity infoEntity = this.infoIrrigaRepository.save(entity);

        return this.responseUtil.sucess(
                infoEntity,
                "Dados atualizados com sucesso",
                HttpStatus.OK
        );
    }

    public  ApiResponse reconfiguraDadosIrrigacaoArduino(InfoIrrigaEntity infoEntity){
        RespostaArduino respostaArduino = this.ble.configurarArduinoComEntidade(infoEntity);
        log.info(respostaArduino.toString());

        if(respostaArduino.ok()){
            return this.responseUtil.sucess(
                    null,
                    "Dados atualizados com sucesso",
                    HttpStatus.OK
            );
        }
        return this.responseUtil.error(respostaArduino.valor(),"Arduino não respondeu a configuração"
                ,HttpStatus.INTERNAL_SERVER_ERROR);
    }



    public InfoIrrigaEntity getFirstInfoIrrigaEntity(){

        InfoIrrigaEntity infoIrrigaEntity = this.infoIrrigaRepository.findFirstByOrderByIdAsc();


        return  infoIrrigaEntity;

    }


}
