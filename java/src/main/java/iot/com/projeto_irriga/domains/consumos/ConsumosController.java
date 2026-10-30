package iot.com.projeto_irriga.domains.consumos;


import iot.com.projeto_irriga.infra.utils.model.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consumo-agua")
public class ConsumosController {

    private final ConsumosService consumosService;

    public ConsumosController(ConsumosService consumosService){
        this.consumosService = consumosService;
    }


    @GetMapping("/analytics")
    public ResponseEntity getConsumoTotal(){
        ApiResponse response = this.consumosService.getConsumosMes();

        return ResponseEntity.status(response.getStatus()).body(response);

    }

    @GetMapping()
    public ResponseEntity pegarRegistrosConsumoAgua(){

        ApiResponse response = this.consumosService.listarUltimasIrrigacoes();


        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @PostMapping()
    public ResponseEntity registrarNovoConsumoAgua(){
        ApiResponse response = this.consumosService.addConsumoAguaPeloArduino(2);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
