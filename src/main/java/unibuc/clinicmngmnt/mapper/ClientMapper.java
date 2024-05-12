package unibuc.clinicmngmnt.mapper;

import org.springframework.stereotype.Component;
import unibuc.clinicmngmnt.domain.Client;
import unibuc.clinicmngmnt.dto.ClientDto;

@Component
public class ClientMapper {
    public Client clientDtoToClient(ClientDto clientDto) {
        return new Client(clientDto.getFirstName(), clientDto.getLastName(), clientDto.getPhone(), clientDto.getEmail());
    }
}
