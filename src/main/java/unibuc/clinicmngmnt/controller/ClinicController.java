package unibuc.clinicmngmnt.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unibuc.clinicmngmnt.dto.ClinicDto;
import unibuc.clinicmngmnt.domain.Clinic;
import unibuc.clinicmngmnt.service.ClinicService;
import unibuc.clinicmngmnt.utility.Utils;
import unibuc.clinicmngmnt.exception.DuplicateClinicException;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/clinics")
public class ClinicController {
    private final ClinicService clinicService;

    // @Autowired implied
    public ClinicController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }
    @GetMapping("/new")
    public String createClinic(Model model) {
        model.addAttribute("clinicDto", new ClinicDto());
        return "clinics/clinicsNew.html";
    }
    @PostMapping
    public String createClinic(
            @Valid
            @ModelAttribute
            ClinicDto clinicDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "clinics/clinicsNew.html";
        }

        try {
            Clinic createdClinic = clinicService.createClinic(clinicDto);
            return "redirect:/clinics";

        } catch(final DuplicateClinicException exception) {
            bindingResult.rejectValue("name", "duplicateClinicName", exception.getMessage());
            return "clinics/clinicsNew.html";

        }

    }

    @GetMapping("/info/{id}")
    public String getClinic(Model model,
                            @PathVariable
                            Long id) {
        Clinic clinic = clinicService.getClinic(id);
        model.addAttribute("object", clinic);

        return "clinics/clinicsInfo.html";

    }

    @GetMapping
    // Default status code 200 (ok)
    public String getAllClinics(Model model,
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
        Page<Clinic> clinicsPage = clinicService.getAllClinics(pageRequest);

        model.addAttribute("page", clinicsPage);

        return "clinics/clinicsList.html";
    }

    @RequestMapping("/delete/{id}")
    public String deleteClinic(
            @PathVariable
            Long id) {
        clinicService.deleteClinic(id);
        return "redirect:/clinics";
    }


}
