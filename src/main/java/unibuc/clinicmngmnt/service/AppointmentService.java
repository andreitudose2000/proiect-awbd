package unibuc.clinicmngmnt.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import unibuc.clinicmngmnt.dto.AppointmentDto;
import unibuc.clinicmngmnt.dto.AppointmentRescheduleDto;
import unibuc.clinicmngmnt.exception.AppointmentsOverlappingException;
import unibuc.clinicmngmnt.exception.NotFoundException;
import unibuc.clinicmngmnt.exception.WrongDatesOrderException;
import unibuc.clinicmngmnt.mapper.AppointmentMapper;
import unibuc.clinicmngmnt.domain.Appointment;
import unibuc.clinicmngmnt.domain.Doctor;
import unibuc.clinicmngmnt.domain.Client;
import unibuc.clinicmngmnt.repository.AppointmentRepository;
import unibuc.clinicmngmnt.repository.DoctorRepository;
import unibuc.clinicmngmnt.repository.ClientRepository;
import unibuc.clinicmngmnt.repository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final ClientRepository clientRepository;
    private final AppointmentMapper appointmentMapper;

    public AppointmentService(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, ClientRepository clientRepository, TaskRepository taskRepository, AppointmentMapper appointmentMapper) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.clientRepository = clientRepository;
        this.appointmentMapper = appointmentMapper;
    }

    public Appointment createAppointment(AppointmentDto appointmentDto) {
        Long doctorId = appointmentDto.getDoctorId();
        Long clientId = appointmentDto.getClientId();

        Appointment appointment = appointmentMapper.appointmentDtoToAppointment(appointmentDto);

        // check if doctor exists
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new NotFoundException("Doctor with ID " + doctorId + " not found."));
        appointment.setDoctor(doctor);

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
        boolean bOverlappingDoctorOrClient = timeOverlappingAppointments.stream().anyMatch(appointment1 -> appointment1.getDoctor().getId() == doctorId || appointment1.getClient().getId() == clientId);

        if (bOverlappingDoctorOrClient) {
            throw new AppointmentsOverlappingException();
        }

        return appointmentRepository.save(appointment);
    }

    public Appointment getAppointment(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment with ID " + id + " not found."));
    }

    public List<Appointment> getAllAppointments(Long clientId, Long doctorId) {
        // create example object
        Appointment appointment = new Appointment();

        //check if client exists
        if (clientId != null) {
            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new NotFoundException("Clientul cu ID " + clientId + " nu a fost gasit."));

            appointment.setClient(client);
        }

        // check if doctor exists
        if (doctorId != null) {
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new NotFoundException("Doctor with ID " + doctorId + " not found."));

            appointment.setDoctor(doctor);
        }

        // ignore id field
        // null fields will be ignored by default
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id");

        return appointmentRepository.findAll(Example.of(appointment, matcher));
    }

    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment with ID " + id + " not found."));
        appointmentRepository.delete(appointment);
    }

    public Appointment rescheduleAppointment(Long id, AppointmentRescheduleDto appointmentRescheduleDto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment with ID " + id + " not found."));

        appointment.setEndDate(appointmentRescheduleDto.getEndDate());
        appointment.setStartDate(appointmentRescheduleDto.getStartDate());

        // check for date in wrong order
        boolean bWrongDatesOrder = appointment.getStartDate().isAfter(appointment.getEndDate());
        if (bWrongDatesOrder) {
            throw new WrongDatesOrderException();
        }

        long doctorId = appointment.getDoctor().getId();
        long clientId = appointment.getClient().getId();

        // check for overlapping appointments
        List<Appointment> appointments = appointmentRepository.findAll();
        List<Appointment> timeOverlappingAppointments = appointments.stream()
                .filter(appointment1 -> appointment1.getStartDate().isBefore(appointment.getEndDate()) && appointment1.getEndDate().isAfter(appointment.getStartDate()) && appointment1.getId() != appointment.getId()).collect(Collectors.toList());
        boolean bOverlappingDoctorOrClient = timeOverlappingAppointments.stream().anyMatch(appointment1 -> appointment1.getDoctor().getId() == doctorId || appointment1.getClient().getId() == clientId);

        if (bOverlappingDoctorOrClient) {
            throw new AppointmentsOverlappingException();
        }

        return appointmentRepository.save(appointment);

    }
}
