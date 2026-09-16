package iot.com.projeto_irriga.domains.consumo_agua;


import iot.com.projeto_irriga.domains.bomba.BombaEntity;
import iot.com.projeto_irriga.domains.usuario.UsuarioEntity;
import iot.com.projeto_irriga.infra.utils.model.EntityModel;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name= "tb_consumo_agua")
public class ConsumoAguaEntity extends EntityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    private double tempoLigado ;

    @ManyToOne
    @JoinColumn(name="bomba_id")
    private BombaEntity bomba;

    @Column
    private Date date ;

    @Column
    private double litrosGastos;

    @ManyToOne
    @JoinColumn(name = "usuario_email")
    private UsuarioEntity usuario;

}
