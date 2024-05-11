package unibuc.clinicmngmnt.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unibuc.clinicmngmnt.domain.Doctor;
import unibuc.clinicmngmnt.dto.ClinicDto;
import unibuc.clinicmngmnt.dto.DoctorDto;
import unibuc.clinicmngmnt.exception.NotFoundException;
import unibuc.clinicmngmnt.exception.SpecialityException;
import unibuc.clinicmngmnt.service.DoctorService;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    // @Autowired implied
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }
    @GetMapping("/new")
    public String createDoctor(Model model) {
        model.addAttribute("doctorDto", new DoctorDto());

        return "doctors/doctorsNew.html";
    }
    @PostMapping
    public String createDoctor(
            @Valid
            @ModelAttribute
            DoctorDto doctorDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "doctors/doctorsNew.html";
        }
        try{
            Doctor createdDoctor = doctorService.createDoctor(doctorDto);
            return "redirect:/doctors";
        } catch (final NotFoundException exception) {
            bindingResult.rejectValue("clinicId", "clinicDoesntExists", exception.getMessage());
            return "doctors/doctorsNew.html";
        }

    }

    @GetMapping("/info/{id}")
    public String getDoctor(
            Model model,
            @PathVariable
            Long id) {

        Doctor doctor = doctorService.getDoctor(id);
        model.addAttribute("object", doctor);

        return "doctors/doctorsInfo.html";
    }

    @GetMapping
    // Default status code 200 (ok)
    public String getAllDoctors(
            Model model,
            @RequestParam(required = false)
            String speciality,
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "1")
            int size,
            @RequestParam(defaultValue = "")
            List<String> sortList,
            @RequestParam(defaultValue = "DESC")
            Sort.Direction sortOrder) {

        Page<Doctor> doctorsPage = doctorService.getAllDoctors(speciality, page, size, sortList, sortOrder);
        model.addAttribute("page", doctorsPage);

        return "doctors/doctorsList.html";
    }

    @RequestMapping("/delete/{id}")
    public String deleteDoctor(
            @PathVariable
            Long id) {
        doctorService.deleteDoctor(id);
        return "redirect:/doctors";
    }


}
