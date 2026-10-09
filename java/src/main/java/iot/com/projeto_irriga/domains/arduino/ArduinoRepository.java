package iot.com.projeto_irriga.domains.arduino;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArduinoRepository extends JpaRepository<ArduinoEntity , UUID> {
}
