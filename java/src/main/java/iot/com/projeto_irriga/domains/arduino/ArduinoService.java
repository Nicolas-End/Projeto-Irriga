package iot.com.projeto_irriga.domains.arduino;

import iot.com.projeto_irriga.domains.consumo_agua.ConsumoAguaService;
import iot.com.projeto_irriga.dto.arduino.ComandoArduino;
import iot.com.projeto_irriga.dto.arduino.MonitoramentoStatusArduino;
import iot.com.projeto_irriga.dto.arduino.RespostaArduino;
import iot.com.projeto_irriga.dto.consumo_agua.ConsumoAguaTotalDTO;
import iot.com.projeto_irriga.enums.arduino.StatusResposta;
import iot.com.projeto_irriga.enums.arduino.TiposComandos;
import iot.com.projeto_irriga.infra.utils.bluetooth.BluetoothSerivce;
import iot.com.projeto_irriga.infra.utils.bluetooth.BluetoothUtil;
import iot.com.projeto_irriga.infra.utils.model.response.ApiResponse;
import iot.com.projeto_irriga.infra.utils.model.response.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class ArduinoService {

    private final ResponseUtil responseUtil;
    private final ConsumoAguaService consumoAguaService;
    private final BluetoothSerivce ble;

    ArduinoService(ResponseUtil responseUtil, ConsumoAguaService consumoAguaService, BluetoothSerivce ble){
        this.responseUtil = responseUtil;
        this.consumoAguaService = consumoAguaService;
        this.ble = ble;
    }

    public ApiResponse StatusTotaisArduino(){
        ApiResponse consumosArduino = consumoAguaService.getConsumoAguaMes();

        if (!consumosArduino.getSucess()){
            return consumosArduino;
        }

        double umidade = (double) this.ble.solicitarUmidadeSoloArduino().valor();

        MonitoramentoStatusArduino monitoramentoStatusArduino = new MonitoramentoStatusArduino((ConsumoAguaTotalDTO) consumosArduino.getDatas(),umidade);

        return this.responseUtil.sucess(monitoramentoStatusArduino,"Status do Arduino Encontrado", HttpStatus.OK);


    }

    public ApiResponse pingTeste(){
        RespostaArduino respostaArduino = this.ble.pingTeste();
        if(respostaArduino.comando().equals("pong")){
            return  this.responseUtil.sucess(respostaArduino,"Conexão estabelcida com sucesso", HttpStatus.OK);
        }
        log.error("Conexão com arduino falhou");
        return this.responseUtil.error(null,"Conexão falhou", HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
