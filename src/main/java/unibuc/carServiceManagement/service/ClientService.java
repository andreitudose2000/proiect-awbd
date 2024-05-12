package unibuc.carServiceManagement.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import unibuc.carServiceManagement.domain.Client;
import unibuc.carServiceManagement.dto.ClientDto;
import unibuc.carServiceManagement.exception.NotFoundException;
import unibuc.carServiceManagement.mapper.ClientMapper;
import unibuc.carServiceManagement.domain.Appointment;
import unibuc.carServiceManagement.repository.AppointmentRepository;
import unibuc.carServiceManagement.repository.ClientRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final AppointmentRepository appointmentRepository;
    private final ClientMapper clientMapper;

    public ClientService(ClientRepository clientRepository, AppointmentRepository appointmentRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.appointmentRepository = appointmentRepository;
        this.clientMapper = clientMapper;
    }

    public Client createClient(ClientDto clientDto) {
        Client client = clientMapper.clientDtoToClient(clientDto);
        return clientRepository.save(client);
    }

    public Client getClient(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Clientul cu ID " + id + " nu a fost gasit."));
    }

    public Page<Client> getAllClients(PageRequest pageRequest) {
        return clientRepository.findAll(pageRequest);
    }

    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Clientul cu ID " + id + " nu a fost gasit."));
        clientRepository.delete(client);
    }

    public List<Appointment> getClientUpcomingAppointments(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Clientul cu ID " + id + " nu a fost gasit."));

        List<Appointment> appointments = appointmentRepository.findAll();
        List<Appointment> futureAppointments = appointments.stream()
                .filter(appointment -> appointment.getClient().getId() == client.getId()
                                    && appointment.getStartDate().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());

        return futureAppointments;

    }

}
