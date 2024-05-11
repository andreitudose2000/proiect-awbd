package unibuc.clinicmngmnt.mapper;

import org.springframework.stereotype.Component;
import unibuc.clinicmngmnt.dto.AppointmentDto;
import unibuc.clinicmngmnt.domain.Appointment;

@Component
public class AppointmentMapper {
    public Appointment appointmentDtoToAppointment(AppointmentDto appointmentDto) {
        return new Appointment(appointmentDto.getStartDate(), appointmentDto.getEndDate(), appointmentDto.getComments());
    }
}
