package at.spengergasse.spring_thymeleaf.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByDeviceId(Long id);

    boolean existsByDeviceIdAndDateTime(Long deviceId, LocalDateTime dateTime);

    boolean existsByPatientIdAndDateTime(Integer patientId, LocalDateTime dateTime);
}