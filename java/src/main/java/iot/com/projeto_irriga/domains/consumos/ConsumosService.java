package iot.com.projeto_irriga.domains.consumos;

import iot.com.projeto_irriga.domains.bomba.BombaEntity;
import iot.com.projeto_irriga.domains.bomba.BombaService;
import iot.com.projeto_irriga.domains.info_irriga.InfoIrrigaEntity;
import iot.com.projeto_irriga.domains.info_irriga.InfoIrrigaService;
import iot.com.projeto_irriga.dto.consumos.ConsumosFormatadoDTO;
import iot.com.projeto_irriga.dto.consumos.ConsumosTotalDTO;
import iot.com.projeto_irriga.infra.utils.bluetooth.BluetoothService;
import iot.com.projeto_irriga.infra.utils.model.response.ApiResponse;
import iot.com.projeto_irriga.infra.utils.model.response.ResponseUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ConsumosService {

    private final ResponseUtil responseUtil;
    private final ConsumosRepository consumosRepository;
    private final InfoIrrigaService infoIrrigaService;
    private final BombaService bombaService;
    private final BluetoothService ble;
    public ConsumosService( BluetoothService ble ,BombaService bombaService, ResponseUtil responseUtil, ConsumosRepository consumosRepository, InfoIrrigaService infoIrrigaService){
        this.responseUtil = responseUtil;
        this.bombaService = bombaService;
        this.ble = ble;
        this.infoIrrigaService = infoIrrigaService;
        this.consumosRepository = consumosRepository;
    }

    public ApiResponse getConsumosMes() {

        Date inicio = getInicioMes();
        Date fim = getInicioProximoMes();


        List<ConsumosEntity> consumos =
                consumosRepository.findByDateBetween(inicio, fim);

        if (consumos.isEmpty()) {
            return responseUtil.sucess(
                    new ConsumosTotalDTO(0.0, 0.0, 0, null, 0.0),
                    "Consumo de agua mes relatado",
                    HttpStatus.OK
            );
        }

        double litrosTotais = this.calcularLitrosTotais(consumos);
        double tempoTotal = this.calcularTempoTotal(consumos);
        Date ultimaVezIrrigado = this.calcularUltimaVezIrrigado(consumos);
        double consumoWatts = this.calcularWattsTotatis(consumos);
        return responseUtil.sucess(
                new ConsumosTotalDTO(litrosTotais, tempoTotal, consumos.size(), ultimaVezIrrigado,consumoWatts),
                "Consumo de agua mes relatado",
                HttpStatus.OK
        );
    }



    @Transactional
    public ApiResponse addConsumoPeloEndpoint(){
        InfoIrrigaEntity infoIrrigaEntity = this.infoIrrigaService.getFirstInfoIrrigaEntity();
        BombaEntity bomba = this.bombaService.getFirstBombaEntity();
        long umidade = (long) this.ble.solicitarUmidadeSoloArduino().valor();

        ConsumosEntity consumosEntity = new ConsumosEntity.Builder(bomba, infoIrrigaEntity,umidade).build();


        this.consumosRepository.save(consumosEntity);

        return this.responseUtil.sucess(null,"Irrigação cadastrada com sucesso",HttpStatus.OK);
    }

    public ApiResponse addConsumoAguaPeloArduino(int vezesIrrigadas){

        InfoIrrigaEntity infoIrrigaEntity = this.infoIrrigaService.getFirstInfoIrrigaEntity();
        BombaEntity bomba = this.bombaService.getFirstBombaEntity();



        List<ConsumosEntity> consumoAguaEntities = new ArrayList<ConsumosEntity>();

        for(int i =  1; i == vezesIrrigadas; i++){

        }

        return this.responseUtil.sucess(null,null,null);

    }

    public ApiResponse listarUltimasIrrigacoes(){

        Pageable ultimos10 = PageRequest.of(0, 10);
        List<ConsumosEntity> listaEntity = consumosRepository.findUltimos10(ultimos10);

        List<ConsumosFormatadoDTO> listaDTO = this.converterConsumoEntityEmDTOFormatado(listaEntity);

        return this.responseUtil.sucess(listaDTO,"Irrigações encontradas", HttpStatus.OK);
    }






    private List<ConsumosFormatadoDTO> converterConsumoEntityEmDTOFormatado(List<ConsumosEntity> entities) {
        return entities.stream()
                .map(e -> new ConsumosFormatadoDTO(
                        0.0,
                        e.getTempoLigado(),
                        e.getLitrosGastos(),
                        e.getConfiguracao(),
                        e.getBomba().getArduino().getArduinoModel(),
                        e.getDate())


                )

                .toList();
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
            List<ConsumosEntity> consumos
    ) {

        double total = 0;

        for (ConsumosEntity consumo : consumos) {
            total += consumo.getLitrosGastos();
        }

        return total;
    }

    private double calcularWattsTotatis(
            List<ConsumosEntity> consumos
    ) {

        double total = 0;

        for (ConsumosEntity consumo : consumos) {
            total += consumo.getWattsUtilizados();
        }

        return total;
    }
    private double calcularTempoTotal(
            List<ConsumosEntity> consumos
    ) {

        double total = 0;

        for (ConsumosEntity consumo : consumos) {
            total += consumo.getTempoLigado();
        }

        return total;
    }

    private Date calcularUltimaVezIrrigado( List<ConsumosEntity> consumos    ){
        Date date = new Date();

        for(ConsumosEntity consumo : consumos){
            if(consumo.getDate().after(date)){
                date = consumo.getDate();
            }
        }

        return date;
    }




}
