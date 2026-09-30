package iot.com.projeto_irriga.domains.consumo_agua;

import iot.com.projeto_irriga.dto.consumo_agua.ConsumoAguaTotalDTO;
import iot.com.projeto_irriga.infra.utils.model.response.ApiResponse;
import iot.com.projeto_irriga.infra.utils.model.response.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
@Service
public class ConsumoAguaService {

    private final ResponseUtil responseUtil;
    private final ConsumoAguaRepository consumoAguaRepository;
    public  ConsumoAguaService( ResponseUtil responseUtil, ConsumoAguaRepository consumoAguaRepository){
        this.responseUtil = responseUtil;
        this.consumoAguaRepository = consumoAguaRepository;
    }

    public ApiResponse getConsumoAguaMes(){
        Calendar inicio = Calendar.getInstance();
        inicio.set(Calendar.DAY_OF_MONTH, 1);
        inicio.set(Calendar.HOUR_OF_DAY, 0);
        inicio.set(Calendar.MINUTE, 0);
        inicio.set(Calendar.SECOND, 0);
        inicio.set(Calendar.MILLISECOND, 0);

        Calendar fim = (Calendar) inicio.clone();
        fim.add(Calendar.MONTH, 1);
        List<ConsumoAguaEntity> consumoAguaEntityList = this.consumoAguaRepository.findByDateBetween(inicio.getTime(), fim.getTime());

        if(consumoAguaEntityList.isEmpty()){
            return this.responseUtil.sucess(new ConsumoAguaTotalDTO(0.0), "Consumo de agua mes relatado", HttpStatus.OK);
        }
        AtomicReference<Double> litrosTotais = new AtomicReference<>((double) 0);

        consumoAguaEntityList.forEach(consumoAguaEntity -> {
            litrosTotais.updateAndGet(v -> new Double((double) (v + consumoAguaEntity.getLitrosGastos())));

        });

        return this.responseUtil.sucess(new ConsumoAguaTotalDTO(litrosTotais.get()),"Consumo de agua mes relatado", HttpStatus.OK);
    }

    public ApiResponse addConsumoAgua(int vezesIrrigadas){

        List<ConsumoAguaEntity> consumoAguaEntities = new ArrayList<ConsumoAguaEntity>();

        for(int i =  1; i == vezesIrrigadas; i++){

            consumoAguaEntities.add(new ConsumoAguaEntity())
        }

        return this.responseUtil.sucess(null,null,null);

    }



}
