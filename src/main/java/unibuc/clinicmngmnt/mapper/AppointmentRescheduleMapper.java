package unibuc.clinicmngmnt.mapper;

import org.springframework.stereotype.Component;
import unibuc.clinicmngmnt.domain.Appointment;
import unibuc.clinicmngmnt.dto.AppointmentDto;

@Component
public class AppointmentRescheduleMapper {
    public Appointment appointmentDtoToAppointment(AppointmentDto appointmentDto) {
        return new Appointment(appointmentDto.getStartDate(), appointmentDto.getEndDate());
    }
}
