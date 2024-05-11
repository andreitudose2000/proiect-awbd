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
import unibuc.clinicmngmnt.domain.Doctor;
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
    public void findDoctor() {
        Doctor doctor = entityManager.find(Doctor.class, 1L);
        assertEquals(doctor.getFirstName(), "Prenume1");
    }

    @Test
    public void saveClinicDoctors() {
        Clinic clinic = new Clinic();
        clinic.setName("ClinicaTest");
        clinic.setAddress("AdresaTest");

        Doctor doctor = new Doctor();
        doctor.setFirstName("PrenumeTest");
        doctor.setLastName("NumeTest");
        doctor.setSpeciality(Speciality.SURGEON);

        clinic.setDoctors(Arrays.asList(doctor));
        entityManager.persist(clinic);
        entityManager.flush();
        entityManager.clear();
    }




}
