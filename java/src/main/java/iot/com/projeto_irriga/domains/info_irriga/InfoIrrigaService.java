package iot.com.projeto_irriga.domains.info_irriga;

import iot.com.projeto_irriga.dto.info_irriga.InfoIrrigaDatasDTO;
import iot.com.projeto_irriga.infra.utils.model.response.ApiResponse;
import iot.com.projeto_irriga.infra.utils.model.response.ResponseUtil;
import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InfoIrrigaService {

    private final InfoIrrigaRepository infoIrrigaRepository;
    private final ResponseUtil responseUtil;

    public InfoIrrigaService (InfoIrrigaRepository infoIrrigaRepository, ResponseUtil responseUtil){
        this.infoIrrigaRepository = infoIrrigaRepository;
        this.responseUtil = responseUtil;
    }


    public ApiResponse setInfoDatas(InfoIrrigaDatasDTO userInfoDatas){

        return this.responseUtil.sucess(null,null,null);

    }



    public ApiResponse getAllInfoIrrigaDatas(){

        InfoIrrigaEntity infoIrrigaEntity = this.getFirstInfoIrrigaEntity();
        if(infoIrrigaEntity == null){

            return  this.responseUtil.sucess(null, "informações Não cadastradas", HttpStatus.OK);
        }

        InfoIrrigaDatasDTO infoIrrigaDatasDTO = new InfoIrrigaDatasDTO(infoIrrigaEntity.getId() ,infoIrrigaEntity.getArduino().getArduinoModel().toString(), infoIrrigaEntity.getUsuario().getEmail(), infoIrrigaEntity.getTipoConfiguacao(),infoIrrigaEntity.getIntervalo(), infoIrrigaEntity.getUmidadeMinima(), infoIrrigaEntity.getDuracaoIrrigacao()) ;


        return  this.responseUtil.sucess(infoIrrigaDatasDTO, "informações Verificadas", HttpStatus.OK);

    }


    public ApiResponse SetAllInfoIrrigaDatas(
            InfoIrrigaDatasDTO dto
    ) {

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
