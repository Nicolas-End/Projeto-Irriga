package iot.com.projeto_irriga.infra.utils;


import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateUtil {
    public static Date GetPresent(){

        return new Date();
    }



    public static String FormatHour(Date time) {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        return formatter.format(time) ;
    }

    public static String FormaterDay(Date time ){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        return formatter.format(time);
    }
}
