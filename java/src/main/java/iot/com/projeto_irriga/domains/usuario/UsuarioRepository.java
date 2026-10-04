package iot.com.projeto_irriga.domains.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;
import java.util.UUID;


@EnableJpaRepositories
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, String> {

    Optional<UsuarioEntity> findByEmail(String email);

    UsuarioEntity findFirstByOrderByEmailAsc();
}
