package unibuc.clinicmngmnt.mapper;

import org.springframework.stereotype.Component;
import unibuc.clinicmngmnt.dto.MedicationDto;
import unibuc.clinicmngmnt.domain.Medication;

@Component
public class MedicationMapper {
    public Medication medicationDtoToMedication(MedicationDto medicationDto) {
        return new Medication(medicationDto.getName(), medicationDto.getBrand());
    }
}
