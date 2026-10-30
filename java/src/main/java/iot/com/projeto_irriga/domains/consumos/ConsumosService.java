package iot.com.projeto_irriga.domains.consumos;

import iot.com.projeto_irriga.domains.bomba.BombaEntity;
import iot.com.projeto_irriga.domains.bomba.BombaService;
import iot.com.projeto_irriga.domains.info_irriga.InfoIrrigaEntity;
import iot.com.projeto_irriga.domains.info_irriga.InfoIrrigaService;
import iot.com.projeto_irriga.dto.arduino.RespostaArduino;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

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
        int umidade = (int) this.ble.solicitarUmidadeSoloArduino().valor();

        ConsumosEntity consumosEntity = new ConsumosEntity.Builder(bomba, infoIrrigaEntity, umidade).build();


        this.consumosRepository.save(consumosEntity);

        return this.responseUtil.sucess(null,"Irrigação cadastrada com sucesso",HttpStatus.OK);
    }

    @Transactional
    public ApiResponse addConsumoAguaPeloArduino(int vezesIrrigadas) {

        // Nada a registrar: evita consultas ao banco e ao Arduino à toa
        if (vezesIrrigadas <= 0) {
            return this.responseUtil.sucess(null, null, null);
        }

        log.info("Cadastrando {} irrigação(ões) no banco de dados", vezesIrrigadas);

        InfoIrrigaEntity infoIrriga = this.infoIrrigaService.getFirstInfoIrrigaEntity();
        BombaEntity bomba = this.bombaService.getFirstBombaEntity();

        // Leitura segura da umidade devolvida pelo Arduino
        RespostaArduino resposta = this.ble.solicitarUmidadeSoloArduino();

        if (resposta == null || !(resposta.valor() instanceof Number numero)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Não foi possível ler a umidade do solo no Arduino");
        }

        int umidade = numero.intValue();

        List<ConsumosEntity> consumos = IntStream.range(0, vezesIrrigadas)
                .mapToObj(i -> new ConsumosEntity.Builder(bomba, infoIrriga, umidade).build())
                .toList();

        this.consumosRepository.saveAll(consumos);

        log.info("{} consumo(s) registrado(s)", consumos.size());

        return this.responseUtil.sucess(null, null, null);
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
                        (int) e.getUmidadeSolo(),
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
