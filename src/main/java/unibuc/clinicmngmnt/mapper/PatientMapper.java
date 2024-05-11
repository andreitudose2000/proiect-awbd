package unibuc.clinicmngmnt.mapper;

import org.springframework.stereotype.Component;
import unibuc.clinicmngmnt.dto.PatientDto;
import unibuc.clinicmngmnt.domain.Patient;

@Component
public class PatientMapper {
    public Patient patientDtoToPatient(PatientDto patientDto) {
        return new Patient(patientDto.getFirstName(), patientDto.getLastName(), patientDto.getPhone(), patientDto.getEmail(), patientDto.getDateOfBirth());
    }
}
