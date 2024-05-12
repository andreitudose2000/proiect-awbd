package unibuc.clinicmngmnt.mapper;

import org.springframework.stereotype.Component;
import unibuc.clinicmngmnt.domain.Appointment;
import unibuc.clinicmngmnt.dto.AppointmentRescheduleDto;

@Component
public class AppointmentRescheduleMapper {
    public void mapToAppointment(AppointmentRescheduleDto appointmentRescheduleDto, Appointment appointment) {
        appointment.setEndDate(appointmentRescheduleDto.getEndDate());
        appointment.setStartDate(appointmentRescheduleDto.getStartDate());
    }
}
