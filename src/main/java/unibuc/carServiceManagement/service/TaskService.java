package unibuc.carServiceManagement.service;

import org.springframework.stereotype.Service;
import unibuc.carServiceManagement.domain.Appointment;
import unibuc.carServiceManagement.domain.Part;
import unibuc.carServiceManagement.domain.Task;
import unibuc.carServiceManagement.dto.TaskAddPartDto;
import unibuc.carServiceManagement.dto.TaskDto;
import unibuc.carServiceManagement.exception.NotFoundException;
import unibuc.carServiceManagement.exception.TaskAlreadyExistingException;
import unibuc.carServiceManagement.mapper.TaskMapper;
import unibuc.carServiceManagement.repository.AppointmentRepository;
import unibuc.carServiceManagement.repository.PartRepository;
import unibuc.carServiceManagement.repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    private final PartRepository partRepository;
    private final TaskMapper taskMapper;
    private final AppointmentRepository appointmentRepository;

    public TaskService(TaskRepository taskRepository, PartRepository partRepository, TaskMapper taskMapper, AppointmentRepository appointmentRepository) {
        this.taskRepository = taskRepository;
        this.partRepository = partRepository;
        this.taskMapper = taskMapper;
        this.appointmentRepository = appointmentRepository;
    }

    public Task createTask(TaskDto taskDto) {
        Long appointmentId = taskDto.getAppointmentId();

        Task task = taskMapper.taskDtoToTask(taskDto);

        // check appointment exists
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Programarea cu ID " + appointmentId + " nu a fost gasita."));

        if (appointment.getTask() != null) {
            throw new TaskAlreadyExistingException(appointmentId);
        }
        task.setAppointment(appointment);

        appointment.setTask(task);

        Appointment createdAppointment = appointmentRepository.save(appointment);

        return createdAppointment.getTask();
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }


    public Task getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Interventia cu ID " + id + " nu a fost gasita"));
    }

    public void addPart(Long id, TaskAddPartDto taskAddPartDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Interventia cu ID " + id + " nu a fost gasita"));

        Part part = partRepository.findById(taskAddPartDto.getPartId())
                .orElseThrow(() -> new NotFoundException("Piesa cu ID " + id + " nu a fost gasita"));

        List<Part> parts = task.getParts();

        parts.add(part);

        task.setParts(parts);

        taskRepository.save(task);
    }

    public void deletePart(Long id, Long partId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Interventia cu ID " + id + " nu a fost gasita"));

        List<Part> parts = task.getParts();
        Part partToRemove = parts.stream().filter(x -> x.getId() == partId).findFirst()
                .orElseThrow(() -> new NotFoundException("Piesa cu ID " + partId + " nu a fost gasita"));

        parts.remove(partToRemove);

        task.setParts(parts);
        taskRepository.save(task);
    }
}
