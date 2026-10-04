package iot.com.projeto_irriga.domains.bomba;

import iot.com.projeto_irriga.domains.info_irriga.InfoIrrigaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.UUID;

@EnableJpaRepositories
public interface BombaRepository extends JpaRepository<BombaEntity, UUID> {

    BombaEntity findFirstByOrderByIdAsc();

}
