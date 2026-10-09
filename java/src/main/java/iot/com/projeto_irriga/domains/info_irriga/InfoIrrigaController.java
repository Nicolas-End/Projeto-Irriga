package iot.com.projeto_irriga.domains.info_irriga;


import iot.com.projeto_irriga.dto.info_irriga.InfoIrrigaDatasDTO;
import iot.com.projeto_irriga.infra.utils.model.response.ApiResponse;
import iot.com.projeto_irriga.infra.utils.model.response.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/info-irriga")
@Slf4j
public class InfoIrrigaController {

    private final InfoIrrigaService infoIrrigaService;

    public InfoIrrigaController(ResponseUtil responseUtil, InfoIrrigaService infoIrrigaService) {

        this.infoIrrigaService = infoIrrigaService;


    }

    @GetMapping
    public ResponseEntity getAllInfoIrriga (){

        ApiResponse response = this.infoIrrigaService.getAllInfoIrrigaDatas();
         return ResponseEntity.status(response.getStatus()).body(response);
    }


    @PutMapping
    public <T> ResponseEntity setIrrigaConfig(@RequestBody InfoIrrigaDatasDTO infoIrrigaDatasDTO){

        log.info("Enviando dados ao Banco dados");


        log.info(String.valueOf(infoIrrigaDatasDTO));

       ApiResponse response =  this.infoIrrigaService.SetAllInfoIrrigaDatas(infoIrrigaDatasDTO);



        return ResponseEntity.status(response.getStatus()).body(response);


    }

}
