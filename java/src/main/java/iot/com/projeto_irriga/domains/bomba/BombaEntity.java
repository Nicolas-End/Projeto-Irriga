package iot.com.projeto_irriga.domains.bomba;


import iot.com.projeto_irriga.domains.arduino.ArduinoEntity;
import iot.com.projeto_irriga.domains.usuario.UsuarioEntity;
import iot.com.projeto_irriga.enums.bomba.ModeloBomba;
import iot.com.projeto_irriga.infra.utils.model.EntityModel;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tb_bomba")
public class BombaEntity extends EntityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    @ManyToOne
    @JoinColumn
    private ArduinoEntity arduino;

    @ManyToOne
    @JoinColumn
    private UsuarioEntity usuario;

    @Column(nullable = false)
    private Double pressao;

    @Column
    private ModeloBomba modeloBomba;


    @Column
    private Double vazao;

}
