package unibuc.clinicmngmnt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unibuc.clinicmngmnt.domain.Prescription;
import unibuc.clinicmngmnt.dto.PrescriptionDto;
import unibuc.clinicmngmnt.exception.PrescriptionAlreadyExistingException;
import unibuc.clinicmngmnt.service.PrescriptionService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/prescriptions")
public class PrescriptionController {
    private final PrescriptionService prescriptionService;

    // @Autowired implied
    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @GetMapping("/new")
    public String createPrescription(
           Model model) {

        model.addAttribute("prescriptionDto", new PrescriptionDto());

        return "prescriptions/prescriptionsNew.html";
    }
    @PostMapping
    public String createPrescription(
            @Valid
            @ModelAttribute
            PrescriptionDto prescriptionDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "prescriptions/prescriptionsNew.html";
        }

        try {
            Prescription createdPrescription = prescriptionService.createPrescription(prescriptionDto);
            return "redirect:/prescriptions";
        } catch(PrescriptionAlreadyExistingException exception) {
            bindingResult.rejectValue("appointmentId", "prescriptionAlreadyAssignedToAppt", exception.getMessage());
            return "prescriptions/prescriptionsNew.html";
        }

    }

    @GetMapping
    // Default status code 200 (ok)
    public String getAllPrescriptions(Model model) {
        List<Prescription> prescriptions = prescriptionService.getAllPrescriptions();
        model.addAttribute("prescriptions", prescriptions);
        return "prescriptions/prescriptionsList.html";
    }

    @GetMapping("/info/{id}")
    public String getPrescriptionMedications(
            Model model,
            @PathVariable
            Long id) {
        Prescription prescription = prescriptionService.getPrescription(id);
        model.addAttribute("medications", prescription.getMedications());

        return "prescriptions/prescriptionsInfo.html";
    }
}
