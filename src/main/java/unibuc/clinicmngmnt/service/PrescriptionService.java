package unibuc.clinicmngmnt.service;

import org.springframework.stereotype.Service;
import unibuc.clinicmngmnt.domain.Appointment;
import unibuc.clinicmngmnt.domain.Prescription;
import unibuc.clinicmngmnt.dto.PrescriptionDto;
import unibuc.clinicmngmnt.exception.NotFoundException;
import unibuc.clinicmngmnt.exception.PrescriptionAlreadyExistingException;
import unibuc.clinicmngmnt.mapper.PrescriptionMapper;
import unibuc.clinicmngmnt.repository.AppointmentRepository;
import unibuc.clinicmngmnt.repository.PrescriptionRepository;

import java.util.List;

@Service
public class PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionMapper prescriptionMapper;
    private final AppointmentRepository appointmentRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository, PrescriptionMapper prescriptionMapper, AppointmentRepository appointmentRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.prescriptionMapper = prescriptionMapper;
        this.appointmentRepository = appointmentRepository;
    }

    public Prescription createPrescription(PrescriptionDto prescriptionDto) {
        Long appointmentId = prescriptionDto.getAppointmentId();

        Prescription prescription = prescriptionMapper.prescriptionDtoToPrescription(prescriptionDto);

        // check appointment exists
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment with ID " + appointmentId + " not found."));

        if (appointment.getPrescription() != null) {
            throw new PrescriptionAlreadyExistingException(appointmentId);
        }
        prescription.setAppointment(appointment);

        appointment.setPrescription(prescription);

        Appointment createdAppointment = appointmentRepository.save(appointment);

        return createdAppointment.getPrescription();
    }

    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }


    public Prescription getPrescription(Long id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prescription with ID " + id + " not found"));
    }
}
