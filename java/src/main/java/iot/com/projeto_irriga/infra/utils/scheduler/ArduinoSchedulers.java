package iot.com.projeto_irriga.infra.utils.scheduler;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
public class ArduinoSchedulers {

    private static ScheduledExecutorService IRRIGATION_SCHEDULE;

    @PostConstruct
    public static void INIT(){




    }

    public static void SET_IRRIGATION_SCHEDULE(int time,TimeUnit timeUnit){

        IRRIGATION_SCHEDULE.shutdown();

        IRRIGATION_SCHEDULE.scheduleAtFixedRate(() -> {
            System.out.println();
        },time,time,timeUnit);
    }

    @PreDestroy
    public static void STOP_IRRIGATION_SCHEDULE(){
        IRRIGATION_SCHEDULE.shutdown();
    }
}
