package iot.com.projeto_irriga.domains.info_irriga;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.UUID;

@EnableJpaRepositories
public interface InfoIrrigaRepository extends JpaRepository<InfoIrrigaEntity, UUID> {

    InfoIrrigaEntity findFirstByOrderByIdAsc();

}
