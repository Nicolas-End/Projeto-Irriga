package iot.com.projeto_irriga.infra.utils.scheduler;

import iot.com.projeto_irriga.domains.arduino.ArduinoService;
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

    private static BluetoothUtil BLE;

    private static ScheduledExecutorService IRRIGATION_SCHEDULE;

    public ArduinoSchedulers (BluetoothUtil ble){
        BLE = ble;
    }


    @PostConstruct
    private static void POST_CONSTRUCT(){
        IRRIGATION_SCHEDULE = Executors.newScheduledThreadPool(1);
    }




    public static void SET_IRRIGATION_SCHEDULE(long time,TimeUnit timeUnit){

        IRRIGATION_SCHEDULE.scheduleAtFixedRate(() -> {
            /*usar o ble para comunicar com o arduino*/
            log.info("Verificando Irrigação");
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
