package unibuc.carServiceManagement.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;
import unibuc.carServiceManagement.domain.security.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}