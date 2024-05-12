package unibuc.clinicmngmnt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unibuc.clinicmngmnt.domain.Appointment;
import unibuc.clinicmngmnt.dto.AppointmentDto;
import unibuc.clinicmngmnt.dto.AppointmentRescheduleDto;
import unibuc.clinicmngmnt.exception.AppointmentsOverlappingException;
import unibuc.clinicmngmnt.exception.WrongDatesOrderException;
import unibuc.clinicmngmnt.service.AppointmentService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    // @Autowired implied
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/new")
    public String createAppointment(Model model) {
        model.addAttribute("appointmentDto", new AppointmentDto());
        return "appointments/appointmentsNew.html";
    }

    @PostMapping
    public String createAppointment(
            @Valid
            @ModelAttribute
            AppointmentDto appointmentDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "appointments/appointmentsNew.html";
        }

        try {
            Appointment createdAppointment = appointmentService.createAppointment(appointmentDto);
            return "redirect:/appointments";

        } catch (AppointmentsOverlappingException | WrongDatesOrderException exception) {
            bindingResult.rejectValue("startDate", "wrongAppointmentDates", exception.getMessage());
            return "appointments/appointmentsNew.html";
        }


    }


    @GetMapping("/info/{id}")
    public String getAppointment(
            Model model,
            @PathVariable
            Long id) {
        Appointment appointment =  appointmentService.getAppointment(id);
        model.addAttribute("object", appointment);

        return "appointments/appointmentsInfo.html";

    }

    @GetMapping
    // Default status code 200 (ok)
    public String getAllAppointments(
            Model model,
            @RequestParam(required = false)
            Long mechanicId,
            @RequestParam(required = false)
            Long clientId) {
        List<Appointment> appointments = appointmentService.getAllAppointments(clientId, mechanicId);
        model.addAttribute("appointments", appointments);
        return "appointments/appointmentsList.html";
    }

    @PostMapping("/reschedule/{id}")
    public String rescheduleAppointment(
            @PathVariable
            Long id,
            @Valid
            @ModelAttribute
            AppointmentRescheduleDto appointmentRescheduleDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "appointments/appointmentsReschedule.html";
        }

        try {
            Appointment createdAppointment = appointmentService.rescheduleAppointment(id,appointmentRescheduleDto);
            return "redirect:/appointments/info/" + id;

        } catch (AppointmentsOverlappingException | WrongDatesOrderException exception) {
            bindingResult.rejectValue("startDate", "wrongAppointmentDates", exception.getMessage());
            return "appointments/appointmentsReschedule.html";
        }

    }
    @GetMapping("/reschedule/{id}")
    public String rescheduleAppointment(
            @PathVariable
            Long id,
            Model model) {
        model.addAttribute("id", id);
        model.addAttribute("appointmentRescheduleDto", new AppointmentRescheduleDto());
        return "appointments/appointmentsReschedule.html";

    }

    @RequestMapping("/delete/{id}")
    public String deleteAppointment(
            @PathVariable
            Long id) {
        appointmentService.deleteAppointment(id);
        return "redirect:/appointments";

    }


}
