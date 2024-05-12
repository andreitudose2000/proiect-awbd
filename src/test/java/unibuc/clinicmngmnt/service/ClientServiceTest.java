package unibuc.clinicmngmnt.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import unibuc.clinicmngmnt.domain.Client;
import unibuc.clinicmngmnt.dto.ClientDto;
import unibuc.clinicmngmnt.mapper.ClientMapper;
import unibuc.clinicmngmnt.repository.AppointmentRepository;
import unibuc.clinicmngmnt.repository.ClientRepository;
import unibuc.clinicmngmnt.exception.NotFoundException;
import unibuc.clinicmngmnt.domain.Appointment;
import unibuc.clinicmngmnt.domain.Doctor;
import unibuc.clinicmngmnt.domain.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientMapper clientMapper;
    @Mock
    private AppointmentRepository appointmentRepository;
    @InjectMocks
    private ClientService clientService;

    private LocalDate currentDate = LocalDate.now();
    private long clientId = 1;
    private ClientDto clientDto = new ClientDto("John", "Proctor", "phone", "email", currentDate);
    private Client client = new Client("John", "Proctor", "phone", "email", currentDate);

    @Test
    @DisplayName("Create client - happy flow")
    void createClientHappy() {
        // Arrange
        Client createdClient = new Client("John", "Proctor", "phone", "email", currentDate);
        createdClient.setId(clientId);

        when(clientMapper.clientDtoToClient(clientDto)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(createdClient);

        // Act
        Client result = clientService.createClient(clientDto);

        // Assert
        assertNotNull(result);
        assertEquals(result.getId(), createdClient.getId());
        assertEquals(result.getFirstName(), createdClient.getFirstName());
        assertEquals(result.getLastName(), createdClient.getLastName());
        assertEquals(result.getPhone(), createdClient.getPhone());
        assertEquals(result.getEmail(), createdClient.getEmail());
    }

    @Test
    @DisplayName("Get client - happy flow")
    void getClientHappy() {
        // Arrange
        client.setId(clientId);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        // Act
        Client result = clientService.getClient(clientId);

        // Assert
        assertNotNull(result);
        assertEquals(result.getId(), client.getId());
        assertEquals(result.getFirstName(), client.getFirstName());
        assertEquals(result.getLastName(), client.getLastName());
        assertEquals(result.getPhone(), client.getPhone());
        assertEquals(result.getEmail(), client.getEmail());
    }

    @Test
    @DisplayName("Get client - Not Found")
    void getClientThrowClientNotFound() {
        // Arrange
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // Act
        NotFoundException exception = assertThrows(NotFoundException.class, () -> clientService.getClient(clientId));

        // Assert
        assertEquals(exception.getMessage(), String.format("Clientul cu ID %d nu a fost gasit.", clientId));
    }

    @Test
    @DisplayName("Delete client - happy flow")
    void deleteClientHappy() {
        // Arrange
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        // Act
        clientService.deleteClient(clientId);

        // Assert
        verify(clientRepository, times(1)).delete(client);
    }

    @Test
    @DisplayName("Delete client - Not Found")
    void deleteClientThrowClientNotFound() {
        // Arrange
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // Act
        NotFoundException exception = assertThrows(NotFoundException.class, () -> clientService.deleteClient(clientId));

        // Assert
        assertEquals(exception.getMessage(), String.format("Clientul cu ID %d nu a fost gasit.", clientId));
    }

    @Test
    @DisplayName("Get client future appointments - happy flow")
    void getClientFutureAppointmentsHappy() {
        // Arrange
        client.setId(clientId);

        List<Appointment> appointments = new ArrayList<Appointment>();
        Doctor doctor = null;
        Task task = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime nextEndDate = LocalDateTime.parse("2023-04-24 08:45", formatter),
                nextStartDate = LocalDateTime.parse("2023-04-24 08:00", formatter),
                passedStartDate = LocalDateTime.parse("2023-04-22 09:45", formatter),
                passedEndDate = LocalDateTime.parse("2023-04-22 09:00", formatter);

        Appointment nextAppointment = new Appointment(1l, client, doctor, nextStartDate, nextEndDate,
                "next - should be returned",
                task),
                passedAppointment = new Appointment(2l, client, doctor, passedStartDate, passedEndDate,
                        "passed - should not be returned", task);
        appointments.add(nextAppointment);
        appointments.add(passedAppointment);

        List<Appointment> returnedAppointments = new ArrayList<Appointment>();
        returnedAppointments.add(nextAppointment);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(appointmentRepository.findAll()).thenReturn(appointments);

        // Act
        List<Appointment> result = clientService.getClientUpcomingAppointments(clientId);

        // Assert
        assertNotNull(result);
        assertArrayEquals(result.toArray(), returnedAppointments.toArray());
    }

    @Test
    @DisplayName("Get client future appointments - client not found")
    void getClientFutureAppointmentsThrowClientNotFound() {
        // Arrange
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // Act
        NotFoundException exception = assertThrows(NotFoundException.class, () -> clientService.getClientUpcomingAppointments(clientId));

        // Assert
        assertEquals(exception.getMessage(), String.format("Clientul cu ID %d nu a fost gasit.", clientId));
    }
}