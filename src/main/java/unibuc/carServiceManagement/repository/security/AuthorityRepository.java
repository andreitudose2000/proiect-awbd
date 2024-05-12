package unibuc.carServiceManagement.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;
import unibuc.carServiceManagement.domain.security.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
}

