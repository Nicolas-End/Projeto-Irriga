package iot.com.projeto_irriga.domains.arduino;

import iot.com.projeto_irriga.domains.consumo_agua.ConsumoAguaService;
import iot.com.projeto_irriga.dto.arduino.ComandoArduino;
import iot.com.projeto_irriga.dto.arduino.MonitoramentoStatusArduino;
import iot.com.projeto_irriga.dto.arduino.RespostaArduino;
import iot.com.projeto_irriga.dto.consumo_agua.ConsumoAguaTotalDTO;
import iot.com.projeto_irriga.enums.arduino.StatusResposta;
import iot.com.projeto_irriga.enums.arduino.TiposComandos;
import iot.com.projeto_irriga.infra.utils.bluetooth.BluetoothUtil;
import iot.com.projeto_irriga.infra.utils.model.response.ApiResponse;
import iot.com.projeto_irriga.infra.utils.model.response.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class ArduinoService {

    private final ResponseUtil responseUtil;
    private final ConsumoAguaService consumoAguaService;

    //private final BluetoothUtil blServico;
    ArduinoService(ResponseUtil responseUtil, ConsumoAguaService consumoAguaService){
        this.responseUtil = responseUtil;
        this.consumoAguaService = consumoAguaService;
        //this.blServico = blServico;
    }

    public ApiResponse StatusTotaisArduino(){
        ApiResponse consumosArduino = consumoAguaService.getConsumoAguaMes();

        if (!consumosArduino.getSucess()){
            return consumosArduino;
        }

        Double umidade = new Double(60);
        /*
         SISTEMA PARA PEDIR A UMIDADE AO ARDUINO

        */

        MonitoramentoStatusArduino monitoramentoStatusArduino = new MonitoramentoStatusArduino((ConsumoAguaTotalDTO) consumosArduino.getDatas(),umidade);

        return this.responseUtil.sucess(monitoramentoStatusArduino,"Status do Arduino Encontrado", HttpStatus.OK);


    }


    /*private ResponseEntity<ApiResponse> git(ComandoArduino comando) {
        try {

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

     */
}
