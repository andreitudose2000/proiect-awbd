package unibuc.carServiceManagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import unibuc.carServiceManagement.domain.Task;
import unibuc.carServiceManagement.dto.TaskAddPartDto;
import unibuc.carServiceManagement.dto.TaskDto;
import unibuc.carServiceManagement.exception.NotFoundException;
import unibuc.carServiceManagement.exception.TaskAlreadyExistingException;
import unibuc.carServiceManagement.service.TaskService;

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
        } catch (final NotFoundException exception) {
            bindingResult.rejectValue("appointmentId", "appointmentDoesntExist", exception.getMessage());
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
        model.addAttribute("taskId", task.getId());
        model.addAttribute("parts", task.getParts());

        return "tasks/tasksInfo.html";
    }

    @GetMapping("/info/{id}/add-part")
    public String addPart(@PathVariable int id, Model model) {

        model.addAttribute("taskId", id);
        model.addAttribute("taskAddPartDto", new TaskAddPartDto());

        return "tasks/tasksAddPart.html";
    }

    @PostMapping("/info/{id}/add-part")
    public String addPart(
            @PathVariable Long id,
            @Valid @ModelAttribute TaskAddPartDto taskAddPartDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "tasks/tasksAddPart.html";
        }

        try {
            taskService.addPart(id, taskAddPartDto);
            return "redirect:/tasks/info/" + id;
        } catch (final NotFoundException exception) {
            bindingResult.rejectValue("partId", "partDoesntExist", exception.getMessage());
            return "tasks/tasksAddPart.html";
        }
    }

    @RequestMapping("/info/{taskId}/delete-part/{partId}")
    public String deletePart(
            @PathVariable Long taskId,
            @PathVariable Long partId) {

        taskService.deletePart(taskId, partId);
        return "redirect:/tasks/info/" + taskId;

    }
}
