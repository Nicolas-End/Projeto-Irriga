package iot.com.projeto_irriga.infra.utils.scheduler;

import iot.com.projeto_irriga.domains.arduino.ArduinoService;
import iot.com.projeto_irriga.domains.consumos.ConsumosService;
import iot.com.projeto_irriga.dto.arduino.RespostaArduino;
import iot.com.projeto_irriga.infra.utils.bluetooth.BluetoothService;
import iot.com.projeto_irriga.infra.utils.bluetooth.BluetoothUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class ArduinoSchedulers {


    private static ScheduledExecutorService IRRIGATION_SCHEDULE;
    private static BluetoothService BLE;
    private static ConsumosService CONSUMOS;

    public ArduinoSchedulers(BluetoothService bluetoothService, ConsumosService consumosService){
        BLE = bluetoothService;
        CONSUMOS = consumosService;
    }


    @PostConstruct
    private static void POST_CONSTRUCT(){

        IRRIGATION_SCHEDULE = Executors.newScheduledThreadPool(1);
    }




    public static void SET_IRRIGATION_SCHEDULE(long time,TimeUnit timeUnit){

        IRRIGATION_SCHEDULE.scheduleAtFixedRate(() -> {

            log.info("Verificando Irrigação");

            RespostaArduino resposta = BLE.verficarVezesIrrigadas();
            if(resposta.ok() && (int) resposta.valor() <= 0){
                CONSUMOS.addConsumoAguaPeloArduino((int) resposta.valor());
            }

            log.info("Foram Cadasta(s) {} irrigação",resposta.valor());

        },time,time,timeUnit);
    }

    @PreDestroy
    public static void STOP_IRRIGATION_SCHEDULE(){



        IRRIGATION_SCHEDULE.shutdown();

        POST_CONSTRUCT();
    }

    public static void setIrrigationSchedule(ScheduledExecutorService irrigationSchedule) {
        IRRIGATION_SCHEDULE = irrigationSchedule;
    }


}
