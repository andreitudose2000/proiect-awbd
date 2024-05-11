package unibuc.clinicmngmnt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unibuc.clinicmngmnt.domain.Medication;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
}
