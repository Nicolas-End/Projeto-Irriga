package iot.com.projeto_irriga.domains.consumos;


import iot.com.projeto_irriga.domains.bomba.BombaEntity;
import iot.com.projeto_irriga.domains.info_irriga.InfoIrrigaEntity;
import iot.com.projeto_irriga.domains.usuario.UsuarioEntity;
import iot.com.projeto_irriga.enums.info_irriga.TipoConfiguacao;
import iot.com.projeto_irriga.infra.utils.DateUtil;
import iot.com.projeto_irriga.infra.utils.model.EntityModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name= "tb_consumos")
@Getter
@Setter
@NoArgsConstructor
public class ConsumosEntity extends EntityModel {

    public ConsumosEntity(Builder builder){
        this.tempoLigado = builder.tempoLigado;
        this.usuario = builder.usuario;
        this.litrosGastos = builder.litrosGastos;
        this.bomba = builder.bomba;
        this.configuracao = builder.infoIrrigaEntity.getTipoConfiguacao();
        this.date = builder.date;
        this.wattsUtilizados = builder.wattsUtilizados;

    }

    public static class Builder{
        Date date;
        private final double tempoLigado;
        private final BombaEntity bomba;
        private final double litrosGastos;
        private final UsuarioEntity usuario;
        private final InfoIrrigaEntity infoIrrigaEntity;
        private final double wattsUtilizados;



        public Builder( BombaEntity bomba,  InfoIrrigaEntity infoIrrigaEntity){
            this.tempoLigado = infoIrrigaEntity.getDuracaoIrrigacao();
            this.bomba = bomba;
            this.litrosGastos = bomba.getVazao() * infoIrrigaEntity.getDuracaoIrrigacao();
            this.usuario = infoIrrigaEntity.getUsuario();
            this.infoIrrigaEntity = infoIrrigaEntity;
            this.wattsUtilizados = infoIrrigaEntity.getDuracaoIrrigacao() *bomba.getPotenciaEletrica();

        }

        public ConsumosEntity build (){
            return  new ConsumosEntity(this);
        }

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    @Column
    private double wattsUtilizados;


    @Column
    private double tempoLigado ;

    @ManyToOne
    @JoinColumn(name="bomba_id")
    private BombaEntity bomba;

    @Column
    private Double umidadeSolo;

    @Column
    private Date date ;

    @Column
    private double litrosGastos;

    @ManyToOne
    @JoinColumn(name = "usuario_email")
    private UsuarioEntity usuario;

    @Column(nullable = true)
    private TipoConfiguacao configuracao;


    @PrePersist
    private void PreDate(){
        this.date = DateUtil.GetPresent();
    }

}
