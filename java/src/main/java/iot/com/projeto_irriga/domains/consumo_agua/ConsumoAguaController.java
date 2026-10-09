package iot.com.projeto_irriga.domains.consumo_agua;


import iot.com.projeto_irriga.infra.utils.model.response.ApiResponse;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consumo-agua")
public class ConsumoAguaController {

    private final ConsumoAguaService consumoAguaService;

    public ConsumoAguaController(ConsumoAguaService consumoAguaService){
        this.consumoAguaService = consumoAguaService;
    }


    @GetMapping()
    public ResponseEntity getConsumoAguaTotal(){
        ApiResponse response = this.consumoAguaService.getConsumoAguaMes();

        return ResponseEntity.status(response.getStatus()).body(response);

    }


    @PostMapping()
    public ResponseEntity registrarNovoConsumoAgua(){
        ApiResponse response = this.consumoAguaService.addConsumoAguaPeloEndpoint();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
