package unibuc.clinicmngmnt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unibuc.clinicmngmnt.domain.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
