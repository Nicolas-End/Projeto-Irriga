package iot.com.projeto_irriga.domains.info_irriga;

import iot.com.projeto_irriga.dto.info_irriga.InfoIrrigaDatasDTO;
import iot.com.projeto_irriga.infra.utils.model.response.ApiResponse;
import iot.com.projeto_irriga.infra.utils.model.response.ResponseUtil;
import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfoIrrigaService {

    private final InfoIrrigaRepository infoIrrigaRepository;
    private final ResponseUtil responseUtil;

    public InfoIrrigaService (InfoIrrigaRepository infoIrrigaRepository, ResponseUtil responseUtil){
        this.infoIrrigaRepository = infoIrrigaRepository;
        this.responseUtil = responseUtil;
    }


    



    public ApiResponse getAllInfoIrrigaDatas(){

        InfoIrrigaEntity infoIrrigaEntity = this.getFirstInfoIrrigaEntity();
        if(infoIrrigaEntity == null){

            return  this.responseUtil.sucess(null, "informações Não cadastradas", HttpStatus.OK);
        }

        InfoIrrigaDatasDTO infoIrrigaDatasDTO = new InfoIrrigaDatasDTO(infoIrrigaEntity.getArduino().getArduinoModel().toString(), infoIrrigaEntity.getUsuario().getEmail(), infoIrrigaEntity.getTipoConfiguacao(),infoIrrigaEntity.getIntervalo(), infoIrrigaEntity.getUmidadeMinima(), infoIrrigaEntity.getDuracaoIrrigacao()) ;


        return  this.responseUtil.sucess(infoIrrigaDatasDTO, "informações Verificadas", HttpStatus.OK);

    }


    public InfoIrrigaEntity getFirstInfoIrrigaEntity(){

        InfoIrrigaEntity infoIrrigaEntity = this.infoIrrigaRepository.findFirstByOrderByIdAsc();


        return  infoIrrigaEntity;

    }


}
