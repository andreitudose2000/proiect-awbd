package unibuc.clinicmngmnt.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import unibuc.clinicmngmnt.domain.Mechanic;
import unibuc.clinicmngmnt.dto.AppointmentDto;
import unibuc.clinicmngmnt.dto.AppointmentRescheduleDto;
import unibuc.clinicmngmnt.exception.AppointmentsOverlappingException;
import unibuc.clinicmngmnt.exception.NotFoundException;
import unibuc.clinicmngmnt.exception.WrongDatesOrderException;
import unibuc.clinicmngmnt.mapper.AppointmentMapper;
import unibuc.clinicmngmnt.domain.Appointment;
import unibuc.clinicmngmnt.domain.Client;
import unibuc.clinicmngmnt.mapper.AppointmentRescheduleMapper;
import unibuc.clinicmngmnt.repository.AppointmentRepository;
import unibuc.clinicmngmnt.repository.MechanicRepository;
import unibuc.clinicmngmnt.repository.ClientRepository;
import unibuc.clinicmngmnt.repository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final MechanicRepository mechanicRepository;
    private final ClientRepository clientRepository;
    private final AppointmentMapper appointmentMapper;
    private final AppointmentRescheduleMapper appointmentRescheduleMapper;

    public AppointmentService(AppointmentRepository appointmentRepository, MechanicRepository mechanicRepository, ClientRepository clientRepository, TaskRepository taskRepository, AppointmentMapper appointmentMapper, AppointmentRescheduleMapper appointmentRescheduleMapper) {
        this.appointmentRepository = appointmentRepository;
        this.mechanicRepository = mechanicRepository;
        this.clientRepository = clientRepository;
        this.appointmentMapper = appointmentMapper;
        this.appointmentRescheduleMapper = appointmentRescheduleMapper;
    }

    public Appointment createAppointment(AppointmentDto appointmentDto) {
        Long mechanicId = appointmentDto.getMechanicId();
        Long clientId = appointmentDto.getClientId();

        Appointment appointment = appointmentMapper.appointmentDtoToAppointment(appointmentDto);

        // check if mechanic exists
        Mechanic mechanic = mechanicRepository.findById(mechanicId)
                .orElseThrow(() -> new NotFoundException("Mecanicul cu ID " + mechanicId + " nu a fost gasit."));
        appointment.setMechanic(mechanic);

        // check if client exists
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Clientul cu ID " + clientId + " nu a fost gasit."));
        appointment.setClient(client);

        //check for date in wrong order
        boolean bWrongDatesOrder = appointment.getStartDate().isAfter(appointment.getEndDate());
        if (bWrongDatesOrder) {
            throw new WrongDatesOrderException();
        }

        // check for overlapping appointments
        List<Appointment> appointments = appointmentRepository.findAll();
        List<Appointment> timeOverlappingAppointments = appointments.stream()
                .filter(appointment1 -> appointment1.getStartDate().isBefore(appointment.getEndDate()) && appointment1.getEndDate().isAfter(appointment.getStartDate())).collect(Collectors.toList());
        boolean bOverlappingMechanicOrClient = timeOverlappingAppointments.stream().anyMatch(appointment1 -> appointment1.getMechanic().getId() == mechanicId || appointment1.getClient().getId() == clientId);

        if (bOverlappingMechanicOrClient) {
            throw new AppointmentsOverlappingException();
        }

        return appointmentRepository.save(appointment);
    }

    public Appointment getAppointment(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Programarea cu ID " + id + " nu a fost gasita."));
    }

    public List<Appointment> getAllAppointments(Long clientId, Long mechanicId) {
        // create example object
        Appointment appointment = new Appointment();

        //check if client exists
        if (clientId != null) {
            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new NotFoundException("Clientul cu ID " + clientId + " nu a fost gasit."));

            appointment.setClient(client);
        }

        // check if mechanic exists
        if (mechanicId != null) {
            Mechanic mechanic = mechanicRepository.findById(mechanicId)
                    .orElseThrow(() -> new NotFoundException("Mecanicul cu ID " + mechanicId + " nu a fost gasit."));

            appointment.setMechanic(mechanic);
        }

        // ignore id field
        // null fields will be ignored by default
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id");

        return appointmentRepository.findAll(Example.of(appointment, matcher));
    }

    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Programarea cu ID " + id + " nu a fost gasit."));
        appointmentRepository.delete(appointment);
    }

    public Appointment rescheduleAppointment(Long id, AppointmentRescheduleDto appointmentRescheduleDto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Programarea cu ID " + id + " nu a fost gasit."));

        appointmentRescheduleMapper.mapToAppointment(appointmentRescheduleDto, appointment);

        // check for date in wrong order
        boolean bWrongDatesOrder = appointment.getStartDate().isAfter(appointment.getEndDate());
        if (bWrongDatesOrder) {
            throw new WrongDatesOrderException();
        }

        long mechanicId = appointment.getMechanic().getId();
        long clientId = appointment.getClient().getId();

        // check for overlapping appointments
        List<Appointment> appointments = appointmentRepository.findAll();
        List<Appointment> timeOverlappingAppointments = appointments.stream()
                .filter(appointment1 -> appointment1.getStartDate().isBefore(appointment.getEndDate()) && appointment1.getEndDate().isAfter(appointment.getStartDate()) && appointment1.getId() != appointment.getId()).collect(Collectors.toList());
        boolean bOverlappingMechanicOrClient = timeOverlappingAppointments.stream().anyMatch(appointment1 -> appointment1.getMechanic().getId() == mechanicId || appointment1.getClient().getId() == clientId);

        if (bOverlappingMechanicOrClient) {
            throw new AppointmentsOverlappingException();
        }

        return appointmentRepository.save(appointment);

    }
}
