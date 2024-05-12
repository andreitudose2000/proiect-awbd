package unibuc.carServiceManagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unibuc.carServiceManagement.dto.PartDto;
import unibuc.carServiceManagement.domain.Part;
import unibuc.carServiceManagement.service.PartService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/parts")
public class PartController {
    private final PartService partService;

    // @Autowired implied
    public PartController(PartService partService) {
        this.partService = partService;
    }


    @GetMapping("/new")
    public String createPart(
            Model model) {

        model.addAttribute("partDto", new PartDto());

        return "parts/partsNew.html";
    }
    @PostMapping
    public String createPart(
            @Valid
            @ModelAttribute
            PartDto partDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "parts/partsNew.html";
        }

        partService.createPart(partDto);
        return "redirect:/parts";
    }

    @GetMapping
    // Default status code 200 (ok)
    public String getAllParts(
            Model model,
            @RequestParam(required = false)
            String brand) {
        List<Part> parts = partService.getAllParts(brand);
        model.addAttribute("parts", parts);
        return "parts/partsList.html";
    }
}
