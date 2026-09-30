package iot.com.projeto_irriga.domains.consumo_agua;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@EnableJpaRepositories
public interface ConsumoAguaRepository extends JpaRepository<ConsumoAguaEntity, UUID>  {
    List<ConsumoAguaEntity> findByDateBetween(Date inicio, Date fim);
}
