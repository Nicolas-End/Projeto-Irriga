package iot.com.projeto_irriga.domains.info_irriga;

import iot.com.projeto_irriga.dto.info_irriga.InfoIrrigaDatasDTO;
import iot.com.projeto_irriga.infra.utils.model.response.ApiResponse;
import iot.com.projeto_irriga.infra.utils.model.response.ResponseUtil;
import iot.com.projeto_irriga.infra.utils.scheduler.ArduinoSchedulers;
import jakarta.annotation.PostConstruct;
import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class InfoIrrigaService {

    private final InfoIrrigaRepository infoIrrigaRepository;
    private final ResponseUtil responseUtil;
    private final ArduinoSchedulers arduinoSchedulers;

    public InfoIrrigaService (InfoIrrigaRepository infoIrrigaRepository, ResponseUtil responseUtil,ArduinoSchedulers arduinoSchedulers){
        this.infoIrrigaRepository = infoIrrigaRepository;
        this.responseUtil = responseUtil;
        this.arduinoSchedulers = arduinoSchedulers;
    }


    @PostConstruct
    public void configurarSchedulerIrrigacao(){

        InfoIrrigaEntity entity = this.getFirstInfoIrrigaEntity();

        ArduinoSchedulers.SET_IRRIGATION_SCHEDULE((long) entity.getIntervalo(), TimeUnit.MINUTES);

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

                ArduinoSchedulers.SET_IRRIGATION_SCHEDULE((long) entity.getIntervalo(),TimeUnit.MINUTES);

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

        this.infoIrrigaRepository.save(entity);

        return this.responseUtil.sucess(
                null,
                "Dados atualizados com sucesso",
                HttpStatus.OK
        );
    }




    public InfoIrrigaEntity getFirstInfoIrrigaEntity(){

        InfoIrrigaEntity infoIrrigaEntity = this.infoIrrigaRepository.findFirstByOrderByIdAsc();


        return  infoIrrigaEntity;

    }


}
