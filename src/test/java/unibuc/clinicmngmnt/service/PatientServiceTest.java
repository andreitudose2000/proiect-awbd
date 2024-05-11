package unibuc.clinicmngmnt.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import unibuc.clinicmngmnt.dto.PatientDto;
import unibuc.clinicmngmnt.mapper.PatientMapper;
import unibuc.clinicmngmnt.domain.Patient;
import unibuc.clinicmngmnt.repository.AppointmentRepository;
import unibuc.clinicmngmnt.repository.PatientRepository;
import unibuc.clinicmngmnt.exception.NotFoundException;
import unibuc.clinicmngmnt.domain.Appointment;
import unibuc.clinicmngmnt.domain.Doctor;
import unibuc.clinicmngmnt.domain.Prescription;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private PatientMapper patientMapper;
    @Mock
    private AppointmentRepository appointmentRepository;
    @InjectMocks
    private PatientService patientService;

    private LocalDate currentDate = LocalDate.now();
    private long patientId = 1;
    private PatientDto patientDto = new PatientDto("John", "Proctor", "phone", "email", currentDate);
    private Patient patient = new Patient("John", "Proctor", "phone", "email", currentDate);

    @Test
    @DisplayName("Create patient - happy flow")
    void createPatientHappy() {
        // Arrange
        Patient createdPatient = new Patient("John", "Proctor", "phone", "email", currentDate);
        createdPatient.setId(patientId);

        when(patientMapper.patientDtoToPatient(patientDto)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(createdPatient);

        // Act
        Patient result = patientService.createPatient(patientDto);

        // Assert
        assertNotNull(result);
        assertEquals(result.getId(), createdPatient.getId());
        assertEquals(result.getFirstName(), createdPatient.getFirstName());
        assertEquals(result.getLastName(), createdPatient.getLastName());
        assertEquals(result.getPhone(), createdPatient.getPhone());
        assertEquals(result.getEmail(), createdPatient.getEmail());
        assertEquals(result.getDateOfBirth(), createdPatient.getDateOfBirth());
    }

    @Test
    @DisplayName("Get patient - happy flow")
    void getPatientHappy() {
        // Arrange
        patient.setId(patientId);

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        // Act
        Patient result = patientService.getPatient(patientId);

        // Assert
        assertNotNull(result);
        assertEquals(result.getId(), patient.getId());
        assertEquals(result.getFirstName(), patient.getFirstName());
        assertEquals(result.getLastName(), patient.getLastName());
        assertEquals(result.getPhone(), patient.getPhone());
        assertEquals(result.getEmail(), patient.getEmail());
        assertEquals(result.getDateOfBirth(), patient.getDateOfBirth());
    }

    @Test
    @DisplayName("Get patient - Not Found")
    void getPatientThrowPatientNotFound() {
        // Arrange
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        // Act
        NotFoundException exception = assertThrows(NotFoundException.class, () -> patientService.getPatient(patientId));

        // Assert
        assertEquals(exception.getMessage(), String.format("Patient with ID %d not found", patientId));
    }

    @Test
    @DisplayName("Delete patient - happy flow")
    void deletePatientHappy() {
        // Arrange
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        // Act
        patientService.deletePatient(patientId);

        // Assert
        verify(patientRepository, times(1)).delete(patient);
    }

    @Test
    @DisplayName("Delete patient - Not Found")
    void deletePatientThrowPatientNotFound() {
        // Arrange
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        // Act
        NotFoundException exception = assertThrows(NotFoundException.class, () -> patientService.deletePatient(patientId));

        // Assert
        assertEquals(exception.getMessage(), String.format("Patient with ID %d not found", patientId));
    }

    @Test
    @DisplayName("Get patient future appointments - happy flow")
    void getPatientFutureAppointmentsHappy() {
        // Arrange
        patient.setId(patientId);

        List<Appointment> appointments = new ArrayList<Appointment>();
        Doctor doctor = null;
        Prescription prescription = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime nextEndDate = LocalDateTime.parse("2023-04-24 08:45", formatter),
                nextStartDate = LocalDateTime.parse("2023-04-24 08:00", formatter),
                passedStartDate = LocalDateTime.parse("2023-04-22 09:45", formatter),
                passedEndDate = LocalDateTime.parse("2023-04-22 09:00", formatter);

        Appointment nextAppointment = new Appointment(1l, patient, doctor, nextStartDate, nextEndDate,
                "next - should be returned",
                prescription),
                passedAppointment = new Appointment(2l, patient, doctor, passedStartDate, passedEndDate,
                        "passed - should not be returned", prescription);
        appointments.add(nextAppointment);
        appointments.add(passedAppointment);

        List<Appointment> returnedAppointments = new ArrayList<Appointment>();
        returnedAppointments.add(nextAppointment);

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findAll()).thenReturn(appointments);

        // Act
        List<Appointment> result = patientService.getPatientUpcomingAppointments(patientId);

        // Assert
        assertNotNull(result);
        assertArrayEquals(result.toArray(), returnedAppointments.toArray());
    }

    @Test
    @DisplayName("Get patient future appointments - patient not found")
    void getPatientFutureAppointmentsThrowPatientNotFound() {
        // Arrange
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        // Act
        NotFoundException exception = assertThrows(NotFoundException.class, () -> patientService.getPatientUpcomingAppointments(patientId));

        // Assert
        assertEquals(exception.getMessage(), String.format("Patient with ID %d not found", patientId));
    }
}