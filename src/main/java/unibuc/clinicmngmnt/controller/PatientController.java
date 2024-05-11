package unibuc.clinicmngmnt.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unibuc.clinicmngmnt.domain.Appointment;
import unibuc.clinicmngmnt.domain.Patient;
import unibuc.clinicmngmnt.dto.PatientDto;
import unibuc.clinicmngmnt.service.PatientService;
import unibuc.clinicmngmnt.utility.Utils;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;

    // @Autowired implied
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/new")
    public String createPatient(Model model) {
        model.addAttribute("patientDto", new PatientDto());

        return "patients/patientsNew.html";
    }

    @PostMapping
    public String createPatient(
            @Valid
            @ModelAttribute
            PatientDto patientDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "patients/patientsNew.html";
        }

        Patient createdPatient = patientService.createPatient(patientDto);
        return "redirect:/patients";

    }

    @GetMapping("/info/{id}")
    public String getPatient(
            Model model,
            @PathVariable
            Long id) {
        Patient patient = patientService.getPatient(id);
        model.addAttribute("object", patient);

        return "patients/patientsInfo.html";
    }

    @GetMapping("/info/{id}/upcomingAppointments")
    public String getPatientUpcomingAppointments(
            Model model,
            @PathVariable
            Long id) {
        List<Appointment> appointments = patientService.getPatientUpcomingAppointments(id);
        model.addAttribute("appointments", appointments);
        return "patients/patientsFutureAppointments.html";
    }

    @GetMapping
    // Default status code 200 (ok)
    public String getAllPatients(
            Model model,
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "1")
            int size,
            @RequestParam(defaultValue = "")
            List<String> sortList,
            @RequestParam(defaultValue = "DESC")
            Sort.Direction sortOrder) {

        List<Sort.Order> orders = Utils.createSortOrder(sortList, sortOrder.toString());

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(orders));
        Page<Patient> patientaPage = patientService.getAllPatients(pageRequest);
        model.addAttribute("page", patientaPage);

        return "patients/patientsList.html";
    }

    @RequestMapping("/delete/{id}")
    public String deletePatient(
            @PathVariable
            Long id) {
        patientService.deletePatient(id);
        return "redirect:/patients";

    }
}
