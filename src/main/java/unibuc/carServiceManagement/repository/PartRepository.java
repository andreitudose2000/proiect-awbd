package unibuc.carServiceManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unibuc.carServiceManagement.domain.Part;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {
}
