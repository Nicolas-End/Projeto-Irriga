package iot.com.projeto_irriga.domains.arduino;

import iot.com.projeto_irriga.domains.consumos.ConsumosService;
import iot.com.projeto_irriga.dto.arduino.MonitoramentoStatusArduino;
import iot.com.projeto_irriga.dto.arduino.RespostaArduino;
import iot.com.projeto_irriga.dto.consumos.ConsumosTotalDTO;
import iot.com.projeto_irriga.infra.utils.bluetooth.BluetoothSerivce;
import iot.com.projeto_irriga.infra.utils.model.response.ApiResponse;
import iot.com.projeto_irriga.infra.utils.model.response.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class ArduinoService {

    private final ResponseUtil responseUtil;
    private final ConsumosService consumosService;
    private final BluetoothSerivce ble;

    ArduinoService(ResponseUtil responseUtil, ConsumosService consumosService, BluetoothSerivce ble){
        this.responseUtil = responseUtil;
        this.consumosService = consumosService;
        this.ble = ble;
    }

    public ApiResponse StatusTotaisArduino(){
        ApiResponse consumosArduino = consumosService.getConsumosMes();

        if (!consumosArduino.getSucess()){
            return consumosArduino;
        }

        double umidade = (double) this.ble.solicitarUmidadeSoloArduino().valor();

        MonitoramentoStatusArduino monitoramentoStatusArduino = new MonitoramentoStatusArduino((ConsumosTotalDTO) consumosArduino.getDatas(),umidade);

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
