package unibuc.clinicmngmnt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unibuc.clinicmngmnt.dto.MedicationDto;
import unibuc.clinicmngmnt.domain.Medication;
import unibuc.clinicmngmnt.dto.PrescriptionDto;
import unibuc.clinicmngmnt.service.MedicationService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/medications")
public class MedicationController {
    private final MedicationService medicationService;

    // @Autowired implied
    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }


    @GetMapping("/new")
    public String createMedication(
            Model model) {

        model.addAttribute("medicationDto", new MedicationDto());

        return "medications/medicationsNew.html";
    }
    @PostMapping
    public String createMedication(
            @Valid
            @ModelAttribute
            MedicationDto medicationDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "medications/medicationsNew.html";
        }

        Medication createdMedication = medicationService.createMedication(medicationDto);
        return "redirect:/medications";
    }

    @GetMapping
    // Default status code 200 (ok)
    public String getAllMedications(
            Model model,
            @RequestParam(required = false)
            String brand) {
        List<Medication> medications = medicationService.getAllMedications(brand);
        model.addAttribute("medications", medications);
        return "medications/medicationsList.html";
    }
}
