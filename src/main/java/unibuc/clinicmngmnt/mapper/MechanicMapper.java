package unibuc.clinicmngmnt.mapper;

import org.springframework.stereotype.Component;
import unibuc.clinicmngmnt.dto.MechanicDto;
import unibuc.clinicmngmnt.domain.Mechanic;

@Component
public class MechanicMapper {
    public Mechanic mechanicDtoToMechanic(MechanicDto mechanicDto) {
        return new Mechanic(mechanicDto.getFirstName(), mechanicDto.getLastName(), mechanicDto.getSpeciality());
    }
}
