package iot.com.projeto_irriga.domains.consumo_agua;


import iot.com.projeto_irriga.domains.bomba.BombaEntity;
import iot.com.projeto_irriga.domains.info_irriga.InfoIrrigaEntity;
import iot.com.projeto_irriga.domains.usuario.UsuarioEntity;
import iot.com.projeto_irriga.infra.utils.model.EntityModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name= "tb_consumo_agua")
@Getter
@Setter
@NoArgsConstructor
public class ConsumoAguaEntity extends EntityModel {

    public ConsumoAguaEntity(Builder builder){
        this.tempoLigado = builder.tempoLigado;
        this.usuario = builder.usuario;
        this.litrosGastos = builder.litrosGastos;
        this.bomba = builder.bomba;
        this.date = builder.date;
        this.duracaoIrrigacao = builder.duracaoIrrigacao; 
    }

    public static class Builder{
        Date date;
        private final double tempoLigado;
        private final BombaEntity bomba;
        private final double litrosGastos;
        private final UsuarioEntity usuario;
        private final Double duracaoIrrigacao ;


        public Builder( BombaEntity bomba, UsuarioEntity usuario, InfoIrrigaEntity infoIrrigaEntity){
            this.tempoLigado = infoIrrigaEntity.getDuracaoIrrigacao();
            this.bomba = bomba;
            this.litrosGastos = bomba.getVazao() * infoIrrigaEntity.getDuracaoIrrigacao();
            this.usuario = usuario;
            this.duracaoIrrigacao = infoIrrigaEntity.getDuracaoIrrigacao();

        }

        public ConsumoAguaEntity build (){
            return  new ConsumoAguaEntity(this);
        }

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    private Double duracaoIrrigacao;



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
