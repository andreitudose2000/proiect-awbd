package unibuc.carServiceManagement.mapper;

import org.springframework.stereotype.Component;
import unibuc.carServiceManagement.domain.Client;
import unibuc.carServiceManagement.dto.ClientDto;

@Component
public class ClientMapper {
    public Client clientDtoToClient(ClientDto clientDto) {
        return new Client(clientDto.getFirstName(), clientDto.getLastName(), clientDto.getPhone(), clientDto.getEmail());
    }
}
