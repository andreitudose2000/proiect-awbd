package unibuc.clinicmngmnt.service;

import unibuc.clinicmngmnt.repository.AppointmentRepository;
import unibuc.clinicmngmnt.domain.Appointment;
import unibuc.clinicmngmnt.domain.Clinic;
import unibuc.clinicmngmnt.domain.Doctor;
import unibuc.clinicmngmnt.domain.Speciality;
import unibuc.clinicmngmnt.domain.Patient;
import unibuc.clinicmngmnt.domain.Task;
import unibuc.clinicmngmnt.dto.AppointmentDto;
import unibuc.clinicmngmnt.mapper.AppointmentMapper;

import unibuc.clinicmngmnt.repository.PatientRepository;
import unibuc.clinicmngmnt.repository.DoctorRepository;

import unibuc.clinicmngmnt.exception.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private AppointmentMapper appointmentMapper;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @InjectMocks
    private AppointmentService appointmentService;

    private long doctorId = 1, patientId = 2, appointmentId = 4;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private LocalDateTime currentDateTimeStart = LocalDateTime.parse("2023-04-23 10:00", formatter);
    private LocalDateTime currentDateTimeEnd = LocalDateTime.parse("2023-04-23 10:45", formatter);
    private LocalDate currentDateLocalDate = LocalDate.now();
    private String comments = "no comments";
    private Clinic clinic = new Clinic("Name", "Address");
    private Doctor doctor = new Doctor("John", "Smith", Speciality.SURGEON, clinic);
    private Patient patient = new Patient("Will", "West", "123456789", "test@email.com", currentDateLocalDate);
    private Task task = new Task("Comments");
    private AppointmentDto appointmentDto = new AppointmentDto(patientId, doctorId, currentDateTimeStart,
            currentDateTimeEnd, comments);
    private Appointment appointmentSaveParam = new Appointment(currentDateTimeStart, currentDateTimeEnd,
            comments);
    private Appointment appointmentMapperReturn = new Appointment(currentDateTimeStart, currentDateTimeEnd,
            comments);
    private Appointment appointment = new Appointment(appointmentId, patient, doctor, currentDateTimeStart,
            currentDateTimeEnd, comments,
            task);
    List<Appointment> existingAppointments = new ArrayList<Appointment>();

    @Test
    @DisplayName("Create appointment - happy flow")
    void createAppointmentHappy() {
        doctor.setId(doctorId);
        patient.setId(patientId);

        appointmentSaveParam.setDoctor(doctor);
        appointmentSaveParam.setPatient(patient);

        when(appointmentMapper.appointmentDtoToAppointment(appointmentDto)).thenReturn(appointmentMapperReturn);
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findAll()).thenReturn(existingAppointments);
        when(appointmentRepository.save(appointmentSaveParam)).thenReturn(appointment);

        Appointment savedAppointment = appointmentService.createAppointment(appointmentDto);

        assertNotNull(savedAppointment);
        assertEquals(appointment.getPatient().getId(), savedAppointment.getPatient().getId());
        assertEquals(appointment.getDoctor().getId(), savedAppointment.getDoctor().getId());
        assertEquals(appointment.getStartDate(), savedAppointment.getStartDate());
        assertEquals(appointment.getEndDate(), savedAppointment.getEndDate());
        assertEquals(appointment.getComments(), savedAppointment.getComments());
    }

    @Test
    @DisplayName("Create appointment - throw doctor not found exception ")
    void createAppointmentThrowDoctorNotFound() {
        when(appointmentMapper.appointmentDtoToAppointment(appointmentDto)).thenReturn(appointmentMapperReturn);
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> appointmentService.createAppointment(appointmentDto));
        assertEquals(exception.getMessage(), String.format("Doctor with ID %d not found.", doctorId));
    }

    @Test
    @DisplayName("Create appointment - throw patient not found exception")
    void createAppointmentThrowPatientNotFound() {
        when(appointmentMapper.appointmentDtoToAppointment(appointmentDto)).thenReturn(appointmentMapperReturn);
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> appointmentService.createAppointment(appointmentDto));
        assertEquals(exception.getMessage(), String.format("Patient with ID %d not found.", patientId));
    }

    @Test
    @DisplayName("Create appointment - throw overlapping appointments exception")
    void createAppointmentThrowAppointmentsOverlapping() {
        doctor.setId(doctorId);
        patient.setId(patientId);
        existingAppointments.add(appointment);

        when(appointmentMapper.appointmentDtoToAppointment(appointmentDto)).thenReturn(appointmentMapperReturn);
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findAll()).thenReturn(existingAppointments);

        AppointmentsOverlappingException exception = assertThrows(AppointmentsOverlappingException.class,
                () -> appointmentService.createAppointment(appointmentDto));
        assertEquals(exception.getMessage(), String.format("Appointment is overlapping with an already existing appointment.", patientId));
    }

    @Test
    @DisplayName("Get appointment - happy Flow")
    void getAppointmentHappy() {
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        Appointment gottenAppointment = appointmentService.getAppointment(appointmentId);

        assertNotNull(gottenAppointment);
        assertEquals(appointment.getPatient().getId(), gottenAppointment.getPatient().getId());
        assertEquals(appointment.getDoctor().getId(), gottenAppointment.getDoctor().getId());
        assertEquals(appointment.getStartDate(), gottenAppointment.getStartDate());
        assertEquals(appointment.getEndDate(), gottenAppointment.getEndDate());
        assertEquals(appointment.getComments(), gottenAppointment.getComments());
        assertEquals(appointment.getTask().getId(), gottenAppointment.getTask().getId());
    }

    @Test
    @DisplayName("Get appointment - throw appointment not found exception")
    void getAppointmentThrowAppointmentNotFound() {
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> appointmentService.getAppointment(appointmentId));
        assertEquals(exception.getMessage(), String.format("Appointment with ID %d not found.", appointmentId));
    }

    @Test
    @DisplayName("Reschedule appointment - throw appointment not found exception")
    void rescheduleAppointmentThrowAppointmentNotFound() {
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> appointmentService.getAppointment(appointmentId));
        assertEquals(exception.getMessage(), String.format("Appointment with ID %d not found.", appointmentId));
    }

    @Test
    @DisplayName("Delete appointment - happy flow")
    void deleteAppointmentHappy() {
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        appointmentService.deleteAppointment(appointmentId);

        verify(appointmentRepository, times(1)).delete(appointment);
    }

    @Test
    @DisplayName("Delete appointment - throw appointment not found exception")
    void deleteAppointmentThrowAppointmentNotFound() {
        long notFoundAppointmentId = appointmentId + 1;

        when(appointmentRepository.findById(notFoundAppointmentId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> appointmentService.deleteAppointment(notFoundAppointmentId));
        assertEquals(exception.getMessage(), String.format("Appointment with ID %d not found.", notFoundAppointmentId));
    }
}