package unibuc.carServiceManagement.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unibuc.carServiceManagement.domain.CarService;
import unibuc.carServiceManagement.dto.CarServiceDto;
import unibuc.carServiceManagement.service.CarServiceService;
import unibuc.carServiceManagement.utility.Utils;
import unibuc.carServiceManagement.exception.DuplicateCarServiceException;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/car-services")
public class CarServiceController {
    private final CarServiceService carServiceService;

    // @Autowired implied
    public CarServiceController(CarServiceService carServiceService) {
        this.carServiceService = carServiceService;
    }
    @GetMapping("/new")
    public String createCarService(Model model) {
        model.addAttribute("carServiceDto", new CarServiceDto());
        return "carService/carServiceNew.html";
    }
    @PostMapping
    public String createCarService(
            @Valid
            @ModelAttribute
            CarServiceDto carServiceDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "carService/carServiceNew.html";
        }

        try {
            CarService createdCarService = carServiceService.createCarService(carServiceDto);
            return "redirect:/car-services";

        } catch(final DuplicateCarServiceException exception) {
            bindingResult.rejectValue("name", "duplicateCarServiceName", exception.getMessage());
            return "carService/carServiceNew.html";

        }

    }

    @GetMapping("/info/{id}")
    public String getCarService(Model model,
                                @PathVariable
                            Long id) {
        CarService carService = carServiceService.getCarService(id);
        model.addAttribute("object", carService);

        return "carService/carServiceInfo.html";

    }

    @GetMapping
    // Default status code 200 (ok)
    public String getAllCarServices(Model model,
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
        Page<CarService> carServicesPage = carServiceService.getAllCarServices(pageRequest);

        model.addAttribute("page", carServicesPage);

        return "carService/carServiceList.html";
    }

    @RequestMapping("/delete/{id}")
    public String deleteCarService(
            @PathVariable
            Long id) {
        carServiceService.deleteCarService(id);
        return "redirect:/car-services";
    }


}
