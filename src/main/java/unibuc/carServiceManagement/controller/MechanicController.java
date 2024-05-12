package unibuc.carServiceManagement.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unibuc.carServiceManagement.domain.Mechanic;
import unibuc.carServiceManagement.dto.MechanicDto;
import unibuc.carServiceManagement.exception.NotFoundException;
import unibuc.carServiceManagement.service.MechanicService;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/mechanics")
public class MechanicController {
    private final MechanicService mechanicService;

    // @Autowired implied
    public MechanicController(MechanicService mechanicService) {
        this.mechanicService = mechanicService;
    }
    @GetMapping("/new")
    public String createMechanic(Model model) {
        model.addAttribute("mechanicDto", new MechanicDto());

        return "mechanics/mechanicsNew.html";
    }
    @PostMapping
    public String createMechanic(
            @Valid
            @ModelAttribute
            MechanicDto mechanicDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "mechanics/mechanicsNew.html";
        }
        try{
            Mechanic createdMechanic = mechanicService.createMechanic(mechanicDto);
            return "redirect:/mechanics";
        } catch (final NotFoundException exception) {
            bindingResult.rejectValue("carServiceId", "carServiceDoesntExist", exception.getMessage());
            return "mechanics/mechanicsNew.html";
        }

    }

    @GetMapping("/info/{id}")
    public String getMechanic(
            Model model,
            @PathVariable
            Long id) {

        Mechanic mechanic = mechanicService.getMechanic(id);
        model.addAttribute("object", mechanic);

        return "mechanics/mechanicsInfo.html";
    }

    @GetMapping
    // Default status code 200 (ok)
    public String getAllMechanics(
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

        Page<Mechanic> mechanicsPage = mechanicService.getAllMechanics(speciality, page, size, sortList, sortOrder);
        model.addAttribute("page", mechanicsPage);

        return "mechanics/mechanicsList.html";
    }

    @RequestMapping("/delete/{id}")
    public String deleteMechanic(
            @PathVariable
            Long id) {
        mechanicService.deleteMechanic(id);
        return "redirect:/mechanics";
    }


}
