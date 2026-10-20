package iot.com.projeto_irriga.domains.consumos;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@EnableJpaRepositories
public interface ConsumosRepository extends JpaRepository<ConsumosEntity, UUID>  {


    //retornar os primeiro n itens que forem predefinidos
    @Query("SELECT c FROM ConsumosEntity c ORDER BY c.date DESC")
    List<ConsumosEntity> findUltimos10(Pageable pageable);

    List<ConsumosEntity> findByDateBetween(Date inicio, Date fim);
}
