package unibuc.carServiceManagement.mapper;

import org.springframework.stereotype.Component;
import unibuc.carServiceManagement.dto.AppointmentDto;
import unibuc.carServiceManagement.domain.Appointment;

@Component
public class AppointmentMapper {
    public Appointment appointmentDtoToAppointment(AppointmentDto appointmentDto) {
        return new Appointment(appointmentDto.getStartDate(), appointmentDto.getEndDate(), appointmentDto.getComments());
    }
}
