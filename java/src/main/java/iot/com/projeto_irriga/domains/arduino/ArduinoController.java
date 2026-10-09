package iot.com.projeto_irriga.domains.arduino;

import iot.com.projeto_irriga.infra.utils.model.response.ApiResponse;
import iot.com.projeto_irriga.infra.utils.model.response.ResponseUtil;
import iot.com.projeto_irriga.infra.utils.scheduler.ArduinoSchedulers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/arduino")
public class ArduinoController {


    private final ResponseUtil responseUtil;
    private final ArduinoService arduinoService;


    public ArduinoController( ResponseUtil responseUtil, ArduinoSchedulers arduinoSchedulers, ArduinoService arduinoService) {
        this.responseUtil = responseUtil;
        this.arduinoService = arduinoService;
    }


    @GetMapping("/status")
    public ResponseEntity pegarStatusArduino(){
        ApiResponse response = this.arduinoService.StatusTotaisArduino();

        return ResponseEntity.status(response.getStatus()).body(response);
    }



}
