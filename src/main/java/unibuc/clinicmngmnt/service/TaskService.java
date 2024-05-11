package unibuc.clinicmngmnt.service;

import org.springframework.stereotype.Service;
import unibuc.clinicmngmnt.domain.Appointment;
import unibuc.clinicmngmnt.domain.Task;
import unibuc.clinicmngmnt.dto.TaskDto;
import unibuc.clinicmngmnt.exception.NotFoundException;
import unibuc.clinicmngmnt.exception.TaskAlreadyExistingException;
import unibuc.clinicmngmnt.mapper.TaskMapper;
import unibuc.clinicmngmnt.repository.AppointmentRepository;
import unibuc.clinicmngmnt.repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final AppointmentRepository appointmentRepository;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, AppointmentRepository appointmentRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.appointmentRepository = appointmentRepository;
    }

    public Task createTask(TaskDto taskDto) {
        Long appointmentId = taskDto.getAppointmentId();

        Task task = taskMapper.taskDtoToTask(taskDto);

        // check appointment exists
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment with ID " + appointmentId + " not found."));

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
}
