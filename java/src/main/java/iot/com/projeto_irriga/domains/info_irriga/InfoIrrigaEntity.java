package iot.com.projeto_irriga.domains.info_irriga;

import iot.com.projeto_irriga.domains.arduino.ArduinoEntity;
import iot.com.projeto_irriga.domains.usuario.UsuarioEntity;
import iot.com.projeto_irriga.enums.info_irriga.TipoConfiguacao;
import iot.com.projeto_irriga.infra.utils.model.EntityModel;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tb_info_irriga")
public class InfoIrrigaEntity extends EntityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "arduino_id")
    private ArduinoEntity arduino;

    @ManyToOne
    @JoinColumn(name = "email_usuario")
    private UsuarioEntity usuario;

    @Column(nullable = false)
    private  boolean irrigarPorUmidade;

    @Column(nullable = false)
    private boolean irrigaPorIntervalo;

    @Column
    private double intervalo;

    @Column
    private double umidadeMinima;

    @Column
    private double duracaoIrrigacao;

    @Column
    private TipoConfiguacao tipoConfiguacao;




}
