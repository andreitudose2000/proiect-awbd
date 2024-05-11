package unibuc.clinicmngmnt.mapper;

import org.springframework.stereotype.Component;
import unibuc.clinicmngmnt.dto.ClinicDto;
import unibuc.clinicmngmnt.domain.Clinic;

@Component
public class ClinicMapper {
    public Clinic clinicDtoToClinic(ClinicDto clinicDto) {
        return new Clinic(clinicDto.getName(), clinicDto.getAddress());
    }
}
