package unibuc.clinicmngmnt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import unibuc.clinicmngmnt.domain.Mechanic;

@Repository
public interface MechanicRepository extends JpaRepository<Mechanic, Long> {
    String FILTER_MECHANICS_ON_SPECIALITY = "select d from Mechanic d where d.speciality like UPPER(?1)";

    @Query(FILTER_MECHANICS_ON_SPECIALITY)
    Page<Mechanic> findBySpeciality(String specialityFilter, Pageable pageable);

    Page<Mechanic> findAll(Pageable page);
}
