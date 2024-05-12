package unibuc.carServiceManagement.mapper;

import org.springframework.stereotype.Component;
import unibuc.carServiceManagement.domain.Appointment;
import unibuc.carServiceManagement.dto.AppointmentRescheduleDto;

@Component
public class AppointmentRescheduleMapper {
    public void mapToAppointment(AppointmentRescheduleDto appointmentRescheduleDto, Appointment appointment) {
        appointment.setEndDate(appointmentRescheduleDto.getEndDate());
        appointment.setStartDate(appointmentRescheduleDto.getStartDate());
    }
}
