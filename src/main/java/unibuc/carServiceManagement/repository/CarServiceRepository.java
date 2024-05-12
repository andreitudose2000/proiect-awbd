package unibuc.carServiceManagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unibuc.carServiceManagement.domain.CarService;

import java.util.Optional;

@Repository
public interface CarServiceRepository extends JpaRepository<CarService, Long> {
    Optional<CarService> findByName(String name);

    Page<CarService> findAll(Pageable page);
}
