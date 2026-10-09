package iot.com.projeto_irriga.domains.consumo_agua;

import iot.com.projeto_irriga.domains.bomba.BombaEntity;
import iot.com.projeto_irriga.domains.bomba.BombaService;
import iot.com.projeto_irriga.domains.info_irriga.InfoIrrigaEntity;
import iot.com.projeto_irriga.domains.info_irriga.InfoIrrigaService;
import iot.com.projeto_irriga.dto.consumo_agua.ConsumoAguaTotalDTO;
import iot.com.projeto_irriga.infra.utils.model.response.ApiResponse;
import iot.com.projeto_irriga.infra.utils.model.response.ResponseUtil;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
@Service
public class ConsumoAguaService {

    private final ResponseUtil responseUtil;
    private final ConsumoAguaRepository consumoAguaRepository;
    private final InfoIrrigaService infoIrrigaService;
    private final BombaService bombaService;
    public  ConsumoAguaService(BombaService bombaService,ResponseUtil responseUtil, ConsumoAguaRepository consumoAguaRepository, InfoIrrigaService infoIrrigaService){
        this.responseUtil = responseUtil;
        this.bombaService = bombaService;
        this.infoIrrigaService = infoIrrigaService;
        this.consumoAguaRepository = consumoAguaRepository;
    }

    public ApiResponse getConsumoAguaMes() {

        Date inicio = getInicioMes();
        Date fim = getInicioProximoMes();


        List<ConsumoAguaEntity> consumos =
                consumoAguaRepository.findByDateBetween(inicio, fim);

        if (consumos.isEmpty()) {
            return responseUtil.sucess(
                    new ConsumoAguaTotalDTO(0.0, 0.0, 0, null),
                    "Consumo de agua mes relatado",
                    HttpStatus.OK
            );
        }

        double litrosTotais = this.calcularLitrosTotais(consumos);
        double tempoTotal = this.calcularTempoTotal(consumos);
        Date ultimaVezIrrigado = this.calcularUltimaVezIrrigado(consumos);
        return responseUtil.sucess(
                new ConsumoAguaTotalDTO(litrosTotais, tempoTotal, consumos.size(), ultimaVezIrrigado),
                "Consumo de agua mes relatado",
                HttpStatus.OK
        );
    }



    @Transactional
    public ApiResponse addConsumoAguaPeloEndpoint(){
        InfoIrrigaEntity infoIrrigaEntity = this.infoIrrigaService.getFirstInfoIrrigaEntity();
        BombaEntity bomba = this.bombaService.getFirstBombaEntity();

        ConsumoAguaEntity consumoAguaEntity = new ConsumoAguaEntity.Builder(bomba, infoIrrigaEntity).build();


        this.consumoAguaRepository.save(consumoAguaEntity);

        return this.responseUtil.sucess(null,"Irrigação cadastrada com sucesso",HttpStatus.OK);
    }

    public ApiResponse addConsumoAguaPeloArduino(int vezesIrrigadas){

        InfoIrrigaEntity infoIrrigaEntity = this.infoIrrigaService.getFirstInfoIrrigaEntity();
        BombaEntity bomba = this.bombaService.getFirstBombaEntity();



        List<ConsumoAguaEntity> consumoAguaEntities = new ArrayList<ConsumoAguaEntity>();

        for(int i =  1; i == vezesIrrigadas; i++){

        }

        return this.responseUtil.sucess(null,null,null);

    }

    private Date getInicioMes() {

        Calendar calendario = Calendar.getInstance();

        calendario.set(Calendar.DAY_OF_MONTH, 1);
        calendario.set(Calendar.HOUR_OF_DAY, 0);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);
        calendario.set(Calendar.MILLISECOND, 0);

        return calendario.getTime();
    }
    private Date getInicioProximoMes() {

        Calendar calendario = Calendar.getInstance();

        calendario.set(Calendar.DAY_OF_MONTH, 1);
        calendario.set(Calendar.HOUR_OF_DAY, 0);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);
        calendario.set(Calendar.MILLISECOND, 0);

        calendario.add(Calendar.MONTH, 1);

        return calendario.getTime();
    }
    private double calcularLitrosTotais(
            List<ConsumoAguaEntity> consumos
    ) {

        double total = 0;

        for (ConsumoAguaEntity consumo : consumos) {
            total += consumo.getLitrosGastos();
        }

        return total;
    }
    private double calcularTempoTotal(
            List<ConsumoAguaEntity> consumos
    ) {

        double total = 0;

        for (ConsumoAguaEntity consumo : consumos) {
            total += consumo.getTempoLigado();
        }

        return total;
    }

    private Date calcularUltimaVezIrrigado( List<ConsumoAguaEntity> consumos    ){
        Date date = new Date();

        for(ConsumoAguaEntity consumo : consumos){
            if(consumo.getDate().after(date)){
                date = consumo.getDate();
            }
        }

        return date;
    }




}
