package unibuc.clinicmngmnt.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import unibuc.clinicmngmnt.dto.PatientDto;
import unibuc.clinicmngmnt.exception.NotFoundException;
import unibuc.clinicmngmnt.mapper.PatientMapper;
import unibuc.clinicmngmnt.domain.Appointment;
import unibuc.clinicmngmnt.domain.Patient;
import unibuc.clinicmngmnt.repository.AppointmentRepository;
import unibuc.clinicmngmnt.repository.PatientRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientMapper patientMapper;

    public PatientService(PatientRepository patientRepository, AppointmentRepository appointmentRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.patientMapper = patientMapper;
    }

    public Patient createPatient(PatientDto patientDto) {
        Patient patient = patientMapper.patientDtoToPatient(patientDto);
        return patientRepository.save(patient);
    }

    public Patient getPatient(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient with ID " + id + " not found"));
    }

    public Page<Patient> getAllPatients(PageRequest pageRequest) {
        return patientRepository.findAll(pageRequest);
    }

    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient with ID " + id + " not found"));
        patientRepository.delete(patient);
    }

    public List<Appointment> getPatientUpcomingAppointments(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient with ID " + id + " not found"));

        List<Appointment> appointments = appointmentRepository.findAll();
        List<Appointment> futureAppointments = appointments.stream().filter(appointment -> appointment.getPatient().getId() == patient.getId() && appointment.getStartDate().isAfter(LocalDateTime.now())).collect(Collectors.toList());

        return futureAppointments;

    }

}
