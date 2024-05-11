package unibuc.clinicmngmnt.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;
import unibuc.clinicmngmnt.domain.security.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
}

