package unibuc.clinicmngmnt.mapper;

import org.springframework.stereotype.Component;
import unibuc.clinicmngmnt.dto.DoctorDto;
import unibuc.clinicmngmnt.domain.Doctor;

@Component
public class DoctorMapper {
    public Doctor doctorDtoToDoctor(DoctorDto doctorDto) {
        return new Doctor(doctorDto.getFirstName(), doctorDto.getLastName(), doctorDto.getSpeciality());
    }
}
