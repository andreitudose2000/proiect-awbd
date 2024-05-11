package unibuc.clinicmngmnt.repository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import unibuc.clinicmngmnt.domain.Patient;

import java.util.List;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace =
        AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("h2")
public class PatientRepositoryTest {
    @Autowired
    private PatientRepository patientRepository;
    @Test
    public void findPatients() {
        List<Patient> products = patientRepository.findAll();
        assertTrue(products.size() >= 2);

    }
    @Test
    public void findPage(){
        Pageable firstPage = PageRequest.of(0, 2);
        Page<Patient> patients = patientRepository.findAll(firstPage);
        Assert.assertTrue(patients.getNumberOfElements() == 2);
    }
}
