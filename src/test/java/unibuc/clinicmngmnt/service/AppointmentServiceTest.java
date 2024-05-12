package unibuc.clinicmngmnt.service;

import unibuc.clinicmngmnt.domain.*;
import unibuc.clinicmngmnt.repository.AppointmentRepository;
import unibuc.clinicmngmnt.domain.Client;
import unibuc.clinicmngmnt.dto.AppointmentDto;
import unibuc.clinicmngmnt.mapper.AppointmentMapper;

import unibuc.clinicmngmnt.repository.ClientRepository;
import unibuc.clinicmngmnt.repository.MechanicRepository;

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
    private ClientRepository clientRepository;
    @Mock
    private MechanicRepository mechanicRepository;
    @InjectMocks
    private AppointmentService appointmentService;

    private long mechanicId = 1, clientId = 2, appointmentId = 4;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private LocalDateTime currentDateTimeStart = LocalDateTime.parse("2023-04-23 10:00", formatter);
    private LocalDateTime currentDateTimeEnd = LocalDateTime.parse("2023-04-23 10:45", formatter);
    private LocalDate currentDateLocalDate = LocalDate.now();
    private String comments = "no comments";
    private Clinic clinic = new Clinic("Name", "Address");
    private Mechanic mechanic = new Mechanic("John", "Smith", Speciality.SURGEON, clinic);
    private Client client = new Client("Will", "West", "123456789", "test@email.com", currentDateLocalDate);
    private Task task = new Task("Comments");
    private AppointmentDto appointmentDto = new AppointmentDto(clientId, mechanicId, currentDateTimeStart,
            currentDateTimeEnd, comments);
    private Appointment appointmentSaveParam = new Appointment(currentDateTimeStart, currentDateTimeEnd,
            comments);
    private Appointment appointmentMapperReturn = new Appointment(currentDateTimeStart, currentDateTimeEnd,
            comments);
    private Appointment appointment = new Appointment(appointmentId, client, mechanic, currentDateTimeStart,
            currentDateTimeEnd, comments,
            task);
    List<Appointment> existingAppointments = new ArrayList<Appointment>();

    @Test
    @DisplayName("Create appointment - happy flow")
    void createAppointmentHappy() {
        mechanic.setId(mechanicId);
        client.setId(clientId);

        appointmentSaveParam.setMechanic(mechanic);
        appointmentSaveParam.setClient(client);

        when(appointmentMapper.appointmentDtoToAppointment(appointmentDto)).thenReturn(appointmentMapperReturn);
        when(mechanicRepository.findById(mechanicId)).thenReturn(Optional.of(mechanic));
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(appointmentRepository.findAll()).thenReturn(existingAppointments);
        when(appointmentRepository.save(appointmentSaveParam)).thenReturn(appointment);

        Appointment savedAppointment = appointmentService.createAppointment(appointmentDto);

        assertNotNull(savedAppointment);
        assertEquals(appointment.getClient().getId(), savedAppointment.getClient().getId());
        assertEquals(appointment.getMechanic().getId(), savedAppointment.getMechanic().getId());
        assertEquals(appointment.getStartDate(), savedAppointment.getStartDate());
        assertEquals(appointment.getEndDate(), savedAppointment.getEndDate());
        assertEquals(appointment.getComments(), savedAppointment.getComments());
    }

    @Test
    @DisplayName("Create appointment - throw mechanic not found exception ")
    void createAppointmentThrowMechanicNotFound() {
        when(appointmentMapper.appointmentDtoToAppointment(appointmentDto)).thenReturn(appointmentMapperReturn);
        when(mechanicRepository.findById(mechanicId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> appointmentService.createAppointment(appointmentDto));
        assertEquals(exception.getMessage(), String.format("Mecanicul cu ID %d nu a fost gasit.", mechanicId));
    }

    @Test
    @DisplayName("Create appointment - throw client not found exception")
    void createAppointmentThrowClientNotFound() {
        when(appointmentMapper.appointmentDtoToAppointment(appointmentDto)).thenReturn(appointmentMapperReturn);
        when(mechanicRepository.findById(mechanicId)).thenReturn(Optional.of(mechanic));
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> appointmentService.createAppointment(appointmentDto));
        assertEquals(exception.getMessage(), String.format("Clientul cu ID %d nu a fost gasit.", clientId));
    }

    @Test
    @DisplayName("Create appointment - throw overlapping appointments exception")
    void createAppointmentThrowAppointmentsOverlapping() {
        mechanic.setId(mechanicId);
        client.setId(clientId);
        existingAppointments.add(appointment);

        when(appointmentMapper.appointmentDtoToAppointment(appointmentDto)).thenReturn(appointmentMapperReturn);
        when(mechanicRepository.findById(mechanicId)).thenReturn(Optional.of(mechanic));
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(appointmentRepository.findAll()).thenReturn(existingAppointments);

        AppointmentsOverlappingException exception = assertThrows(AppointmentsOverlappingException.class,
                () -> appointmentService.createAppointment(appointmentDto));
        assertEquals(exception.getMessage(), String.format("Appointment is overlapping with an already existing appointment.", clientId));
    }

    @Test
    @DisplayName("Get appointment - happy Flow")
    void getAppointmentHappy() {
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        Appointment gottenAppointment = appointmentService.getAppointment(appointmentId);

        assertNotNull(gottenAppointment);
        assertEquals(appointment.getClient().getId(), gottenAppointment.getClient().getId());
        assertEquals(appointment.getMechanic().getId(), gottenAppointment.getMechanic().getId());
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