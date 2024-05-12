package unibuc.carServiceManagement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import static org.junit.jupiter.api.Assertions.*;

import unibuc.carServiceManagement.domain.CarService;
import unibuc.carServiceManagement.domain.Mechanic;
import unibuc.carServiceManagement.domain.Speciality;

import java.util.Arrays;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("h2")
@Rollback(true)
public class EntityManagerTest {
    @Autowired
    private EntityManager entityManager;

    @Test
    public void findCarService() {
        CarService carService = entityManager.find(CarService.class, 1L);
        assertEquals(carService.getName(), "Service1");
    }

    @Test
    public void findMechanic() {
        Mechanic mechanic = entityManager.find(Mechanic.class, 1L);
        assertEquals(mechanic.getFirstName(), "Prenume1");
    }

    @Test
    public void saveCarServiceMechanics() {
        CarService carService = new CarService();
        carService.setName("ServiceTest");
        carService.setAddress("AdresaTest");

        Mechanic mechanic = new Mechanic();
        mechanic.setFirstName("PrenumeTest");
        mechanic.setLastName("NumeTest");
        mechanic.setSpeciality(Speciality.SUSPENSION);

        carService.setMechanics(Arrays.asList(mechanic));
        entityManager.persist(carService);
        entityManager.flush();
        entityManager.clear();
    }




}
