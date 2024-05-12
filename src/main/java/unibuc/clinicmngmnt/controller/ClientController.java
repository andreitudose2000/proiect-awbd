package unibuc.clinicmngmnt.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unibuc.clinicmngmnt.domain.Appointment;
import unibuc.clinicmngmnt.domain.Client;
import unibuc.clinicmngmnt.dto.ClientDto;
import unibuc.clinicmngmnt.service.ClientService;
import unibuc.clinicmngmnt.utility.Utils;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;

    // @Autowired implied
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/new")
    public String createClient(Model model) {
        model.addAttribute("clientDto", new ClientDto());

        return "clients/clientsNew.html";
    }

    @PostMapping
    public String createClient(
            @Valid
            @ModelAttribute
            ClientDto clientDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "clients/clientsNew.html";
        }

        clientService.createClient(clientDto);
        return "redirect:/clients";

    }

    @GetMapping("/info/{id}")
    public String getClient(
            Model model,
            @PathVariable
            Long id) {
        Client client = clientService.getClient(id);
        model.addAttribute("object", client);

        return "clients/clientsInfo.html";
    }

    @GetMapping("/info/{id}/upcomingAppointments")
    public String getClientUpcomingAppointments(
            Model model,
            @PathVariable
            Long id) {
        List<Appointment> appointments = clientService.getClientUpcomingAppointments(id);
        model.addAttribute("appointments", appointments);
        return "clients/clientsFutureAppointments.html";
    }

    @GetMapping
    // Default status code 200 (ok)
    public String getAllClients(
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
        Page<Client> clientsPage = clientService.getAllClients(pageRequest);
        model.addAttribute("page", clientsPage);

        return "clients/clientsList.html";
    }

    @RequestMapping("/delete/{id}")
    public String deleteClient(
            @PathVariable
            Long id) {
        clientService.deleteClient(id);
        return "redirect:/clients";

    }
}
