package iot.com.projeto_irriga.domains.arduino;

import iot.com.projeto_irriga.infra.utils.model.response.ApiResponse;
import iot.com.projeto_irriga.infra.utils.model.response.ResponseUtil;
import iot.com.projeto_irriga.infra.utils.scheduler.ArduinoSchedulers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/arduino")
public class ArduinoController {


    private final ResponseUtil responseUtil;

    public ArduinoController( ResponseUtil responseUtil, ArduinoSchedulers arduinoSchedulers) {
        this.responseUtil = responseUtil;


    }


    @GetMapping("/get/all-infos")
    public ResponseEntity<ApiResponse> getAllInfos(){
        ArduinoSchedulers.SET_IRRIGATION_SCHEDULE(3, TimeUnit.SECONDS);

        return ResponseEntity.ok().body(null);
    }



}
