package iot.com.projeto_irriga.domains.consumo_agua;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@EnableJpaRepositories
public interface ConsumoAguaRepository extends JpaRepository<ConsumoAguaEntity, UUID>  {


    //retornar os primeiro n itens que forem predefinidos
    @Query("SELECT c FROM ConsumoAguaEntity c ORDER BY c.date DESC")
    List<ConsumoAguaEntity> findUltimos10(Pageable pageable);

    List<ConsumoAguaEntity> findByDateBetween(Date inicio, Date fim);
}
