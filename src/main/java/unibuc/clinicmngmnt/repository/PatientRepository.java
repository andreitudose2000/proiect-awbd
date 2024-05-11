package unibuc.clinicmngmnt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unibuc.clinicmngmnt.domain.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Page<Patient> findAll(Pageable page);
}
