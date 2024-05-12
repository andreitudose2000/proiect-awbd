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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import unibuc.clinicmngmnt.domain.Client;

import java.util.List;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace =
        AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("h2")
public class ClientRepositoryTest {
    @Autowired
    private ClientRepository clientRepository;
    @Test
    public void findClients() {
        List<Client> products = clientRepository.findAll();
        assertTrue(products.size() >= 2);

    }
    @Test
    public void findPage(){
        Pageable firstPage = PageRequest.of(0, 2);
        Page<Client> clients = clientRepository.findAll(firstPage);
        Assert.assertTrue(clients.getNumberOfElements() == 2);
    }
}
