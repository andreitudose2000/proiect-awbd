package unibuc.clinicmngmnt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unibuc.clinicmngmnt.domain.Task;
import unibuc.clinicmngmnt.dto.TaskDto;
import unibuc.clinicmngmnt.exception.TaskAlreadyExistingException;
import unibuc.clinicmngmnt.service.TaskService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TasksController {
    private final TaskService taskService;

    // @Autowired implied
    public TasksController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/new")
    public String createTask(
           Model model) {

        model.addAttribute("taskDto", new TaskDto());

        return "tasks/tasksNew.html";
    }
    @PostMapping
    public String createTask(
            @Valid
            @ModelAttribute
            TaskDto taskDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "tasks/tasksNew.html";
        }

        try {
            taskService.createTask(taskDto);
            return "redirect:/tasks";
        } catch(TaskAlreadyExistingException exception) {
            bindingResult.rejectValue("appointmentId", "taskAlreadyAssignedToAppt", exception.getMessage());
            return "tasks/tasksNew.html";
        }

    }

    @GetMapping
    // Default status code 200 (ok)
    public String getAllTasks(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "tasks/tasksList.html";
    }

    @GetMapping("/info/{id}")
    public String getTaskParts(
            Model model,
            @PathVariable
            Long id) {
        Task task = taskService.getTask(id);
        model.addAttribute("parts", task.getParts());

        return "tasks/tasksInfo.html";
    }
}
