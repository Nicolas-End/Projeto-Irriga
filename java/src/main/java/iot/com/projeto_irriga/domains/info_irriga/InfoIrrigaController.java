package iot.com.projeto_irriga.domains.info_irriga;


import iot.com.projeto_irriga.infra.utils.model.response.ApiResponse;
import iot.com.projeto_irriga.infra.utils.model.response.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info-irriga")

public class InfoIrrigaController {

    private final InfoIrrigaService infoIrrigaService;

    public InfoIrrigaController(ResponseUtil responseUtil, InfoIrrigaService infoIrrigaService) {

        this.infoIrrigaService = infoIrrigaService;


    }


     @GetMapping("/all")
    public ResponseEntity getAllInfoIrriga (){
        ApiResponse response = this.infoIrrigaService.getAllInfoIrrigaDatas();
         return ResponseEntity.status(response.getStatus()).body(response);
    }



}
