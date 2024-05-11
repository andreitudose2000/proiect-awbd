package unibuc.clinicmngmnt.mapper;

import org.springframework.stereotype.Component;
import unibuc.clinicmngmnt.dto.PrescriptionDto;
import unibuc.clinicmngmnt.domain.Prescription;

@Component
public class PrescriptionMapper {
    public Prescription prescriptionDtoToPrescription(PrescriptionDto prescriptionDto) {
        return new Prescription(prescriptionDto.getComments());
    }
}
