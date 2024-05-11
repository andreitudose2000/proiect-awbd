package unibuc.clinicmngmnt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unibuc.clinicmngmnt.domain.Part;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {
}
