package iot.com.projeto_irriga.domains.usuario;


import iot.com.projeto_irriga.infra.utils.model.EntityModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_usuario")
@Getter
@NoArgsConstructor
@Setter
public class UsuarioEntity extends EntityModel {

    @Id
    private String email;

    @Column(name = "nome")
    private String name;

    @Column(name = "senha")
    private String senha;


}
