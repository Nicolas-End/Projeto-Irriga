package iot.com.projeto_irriga.domains.arduino;


import iot.com.projeto_irriga.domains.usuario.UsuarioEntity;
import iot.com.projeto_irriga.enums.arduino.ModeloArduino;
import iot.com.projeto_irriga.infra.utils.model.EntityModel;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name="tb_arduino")
public class ArduinoEntity extends EntityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private UsuarioEntity user;

    @Column(name = "modelo_arduino")
    private ModeloArduino arduinoModel;

    @Column(name = "endereço_mac",unique = true)
    private String macAddress;

}
