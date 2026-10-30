package iot.com.projeto_irriga.domains.arduino;

import iot.com.projeto_irriga.domains.consumos.ConsumosService;
import iot.com.projeto_irriga.domains.info_irriga.InfoIrrigaService;
import iot.com.projeto_irriga.dto.arduino.MonitoramentoStatusArduino;
import iot.com.projeto_irriga.dto.arduino.RespostaArduino;
import iot.com.projeto_irriga.dto.consumos.ConsumosTotalDTO;
import iot.com.projeto_irriga.infra.utils.bluetooth.BluetoothService;
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
    private final InfoIrrigaService infoIrrigaService;
    private final BluetoothService ble;

    ArduinoService( InfoIrrigaService infoIrrigaService ,ResponseUtil responseUtil, ConsumosService consumosService, BluetoothService ble){
        this.responseUtil = responseUtil;
        this.infoIrrigaService = infoIrrigaService;
        this.consumosService = consumosService;
        this.ble = ble;
    }

    public ApiResponse StatusTotaisArduino(){
        ApiResponse consumosArduino = consumosService.getConsumosMes();

        if (!consumosArduino.getSucess()){
            return consumosArduino;
        }

        int umidade = (int) this.ble.solicitarUmidadeSoloArduino().valor();
        MonitoramentoStatusArduino monitoramentoStatusArduino = new MonitoramentoStatusArduino((ConsumosTotalDTO) consumosArduino.getDatas(),umidade);

        return this.responseUtil.sucess(monitoramentoStatusArduino,"Status do Arduino Encontrado", HttpStatus.OK);


    }

    public ApiResponse pingTeste(){
        RespostaArduino respostaArduino = this.ble.pingTeste();

        if(respostaArduino.valor().equals("pong")){
            return  this.responseUtil.sucess(respostaArduino,"Conexão estabelcida com sucesso", HttpStatus.OK);
        }
        log.error("Conexão com arduino falhou");
        log.info(respostaArduino.toString());
        return this.responseUtil.error(null,"Conexão falhou", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ApiResponse configuarArduino(){
        RespostaArduino respostaArduino = this.ble.configurarArduino();
        this.infoIrrigaService.configurarSchedulerIrrigacao();
        log.info(respostaArduino.toString());
        if (respostaArduino.ok()){
            return this.responseUtil.sucess(respostaArduino, "Arduino configurado com sucesso",HttpStatus.OK);
        }
        return this.responseUtil.error(respostaArduino, "Arduino não configurado", HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
