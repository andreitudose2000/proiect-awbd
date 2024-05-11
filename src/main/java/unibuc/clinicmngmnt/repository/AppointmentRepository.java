package unibuc.clinicmngmnt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unibuc.clinicmngmnt.domain.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
