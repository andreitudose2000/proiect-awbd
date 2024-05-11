package unibuc.clinicmngmnt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import unibuc.clinicmngmnt.domain.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    String FILTER_DOCTORS_ON_SPECIALITY = "select d from Doctor d where d.speciality like UPPER(?1)";

    @Query(FILTER_DOCTORS_ON_SPECIALITY)
    Page<Doctor> findBySpeciality(String specialityFilter, Pageable pageable);

    Page<Doctor> findAll(Pageable page);
}
