package unibuc.carServiceManagement.mapper;

import org.springframework.stereotype.Component;
import unibuc.carServiceManagement.dto.MechanicDto;
import unibuc.carServiceManagement.domain.Mechanic;

@Component
public class MechanicMapper {
    public Mechanic mechanicDtoToMechanic(MechanicDto mechanicDto) {
        return new Mechanic(mechanicDto.getFirstName(), mechanicDto.getLastName(), mechanicDto.getSpeciality());
    }
}
