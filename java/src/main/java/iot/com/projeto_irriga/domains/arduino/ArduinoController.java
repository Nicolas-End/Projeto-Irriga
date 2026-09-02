package iot.com.projeto_irriga.domains.arduino;

import iot.com.projeto_irriga.dto.arduino.ComandoArduino;
import iot.com.projeto_irriga.dto.arduino.RespostaArduino;
import iot.com.projeto_irriga.enums.arduino.StatusResposta;
import iot.com.projeto_irriga.enums.arduino.TiposComandos;
import iot.com.projeto_irriga.utils.response.ApiResponse;
import iot.com.projeto_irriga.utils.response.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/arduino")
public class ArduinoController {

    private final ArduinoService servico;
    private final ResponseUtil responseUtil;


    public ArduinoController(ArduinoService servico, ResponseUtil responseUtil) {
        this.responseUtil = responseUtil;
        this.servico = servico;
    }


    @PostMapping ("/get/all-infos")
    public ResponseEntity<ApiResponse> getAllInfos(){
        return executar(new ComandoArduino(TiposComandos.INFORMATIVO, "all"));

    }
   

    private ResponseEntity<ApiResponse> executar(ComandoArduino comando) {
        try {
            RespostaArduino resposta = servico.enviarEEsperarResposta(comando, 3000);
            if(resposta.status().equals(StatusResposta.OK)) {
                ApiResponse apiResponse = this.responseUtil.sucess(resposta, resposta.message(), HttpStatus.OK);
        }
            ApiResponse apiResponse = this.responseUtil.error(resposta, resposta.message(), HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);

        } catch (Exception e) {
            ApiResponse apiResponse = this.responseUtil.error(null, null, HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
        }
    }
}
