package unibuc.clinicmngmnt.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import unibuc.clinicmngmnt.domain.*;
import unibuc.clinicmngmnt.dto.PrescriptionDto;
import unibuc.clinicmngmnt.mapper.PrescriptionMapper;
import unibuc.clinicmngmnt.repository.AppointmentRepository;
import unibuc.clinicmngmnt.repository.PrescriptionRepository;
import unibuc.clinicmngmnt.domain.Part;
import unibuc.clinicmngmnt.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PrescriptionServiceTest {
    @Mock
    private PrescriptionRepository prescriptionRepository;
    @Mock
    private PrescriptionMapper prescriptionMapper;
    @Mock
    private AppointmentRepository appointmentRepository;
    @InjectMocks
    private PrescriptionService prescriptionService;

    long appointmentId = 1, prescriptionId = 2;
    String comments = "Comments";
    LocalDateTime startDate = LocalDateTime.now(), endDate = LocalDateTime.now();
    private List<Part> parts = new ArrayList<Part>();
    private Appointment appointment = new Appointment(appointmentId, (Patient) null, (Doctor) null, startDate, endDate, "no comments", (Prescription) null);
    private PrescriptionDto prescriptionDto = new PrescriptionDto(appointmentId, comments);
    private Prescription prescription = new Prescription(prescriptionId, appointment, comments, parts);

    @Test
    @DisplayName("Create prescription - happy flow")
    void createPrescriptionHappy() {
        // Arrange
        when(prescriptionMapper.prescriptionDtoToPrescription(prescriptionDto)).thenReturn(prescription);
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);

        // Act
        Prescription result = prescriptionService.createPrescription(prescriptionDto);

        // Assert
        assertNotNull(result);
        assertEquals(result.getId(), appointment.getPrescription().getId());
    }

    @Test
    @DisplayName("Create prescription - appointment not found")
    void createPrescriptionThrowAppointmentNotFound() {
        // Arrange
        when(prescriptionMapper.prescriptionDtoToPrescription(prescriptionDto)).thenReturn(prescription);
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        // Act
        NotFoundException exception = assertThrows(NotFoundException.class, () -> prescriptionService.createPrescription(prescriptionDto));

        // Assert
        assertNotNull(exception);
        assertEquals(exception.getMessage(), String.format("Appointment with ID %d not found.", appointmentId));
    }

    @Test
    @DisplayName("Get all prescriptions - happy flow")
    void getAllPrescriptions() {
        // Arrange
        List<Prescription> prescriptions = new ArrayList<Prescription>();
        prescriptions.add(prescription);

        when(prescriptionRepository.findAll()).thenReturn(prescriptions);

        // Act
        List<Prescription> result = prescriptionService.getAllPrescriptions();

        // Assert
        assertNotNull(result);
        assertArrayEquals(result.toArray(), prescriptions.toArray());
    }
}