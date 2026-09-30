package iot.com.projeto_irriga.domains.arduino;

import iot.com.projeto_irriga.dto.arduino.ComandoArduino;
import iot.com.projeto_irriga.dto.arduino.RespostaArduino;
import iot.com.projeto_irriga.enums.arduino.StatusResposta;
import iot.com.projeto_irriga.infra.utils.model.response.ApiResponse;
import iot.com.projeto_irriga.infra.utils.model.response.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


/*@Service
public class ArduinoService {

    private final ResponseUtil responseUtil;
    private final ArduinoBluetooth blServico;
    ArduinoService(ResponseUtil responseUtil, ArduinoBluetooth blServico){
        this.responseUtil = responseUtil;
        this.blServico = blServico;
    }




    private ResponseEntity<ApiResponse> git(ComandoArduino comando) {
        try {
            ArduinoBluetooth servico;
            RespostaArduino resposta = this.blServico.enviarEEsperarResposta(comando, 3000);
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
}*/
