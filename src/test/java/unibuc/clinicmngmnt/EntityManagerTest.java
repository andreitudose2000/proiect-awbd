package unibuc.clinicmngmnt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import static org.junit.jupiter.api.Assertions.*;
import unibuc.clinicmngmnt.domain.Clinic;
import unibuc.clinicmngmnt.domain.Mechanic;
import unibuc.clinicmngmnt.domain.Speciality;

import java.util.Arrays;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("h2")
@Rollback(true)
public class EntityManagerTest {
    @Autowired
    private EntityManager entityManager;

    @Test
    public void findClinic() {
        Clinic clinic = entityManager.find(Clinic.class, 1L);
        assertEquals(clinic.getName(), "Clinica1");
    }

    @Test
    public void findMechanic() {
        Mechanic mechanic = entityManager.find(Mechanic.class, 1L);
        assertEquals(mechanic.getFirstName(), "Prenume1");
    }

    @Test
    public void saveClinicMechanics() {
        Clinic clinic = new Clinic();
        clinic.setName("ClinicaTest");
        clinic.setAddress("AdresaTest");

        Mechanic mechanic = new Mechanic();
        mechanic.setFirstName("PrenumeTest");
        mechanic.setLastName("NumeTest");
        mechanic.setSpeciality(Speciality.SURGEON);

        clinic.setMechanics(Arrays.asList(mechanic));
        entityManager.persist(clinic);
        entityManager.flush();
        entityManager.clear();
    }




}
