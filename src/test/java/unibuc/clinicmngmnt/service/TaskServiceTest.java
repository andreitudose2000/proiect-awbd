package unibuc.clinicmngmnt.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import unibuc.clinicmngmnt.domain.*;
import unibuc.clinicmngmnt.dto.TaskDto;
import unibuc.clinicmngmnt.mapper.TaskMapper;
import unibuc.clinicmngmnt.repository.AppointmentRepository;
import unibuc.clinicmngmnt.repository.TaskRepository;
import unibuc.clinicmngmnt.domain.Part;
import unibuc.clinicmngmnt.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private AppointmentRepository appointmentRepository;
    @InjectMocks
    private TaskService taskService;

    long appointmentId = 1, taskId = 2;
    String comments = "Comments";
    LocalDateTime startDate = LocalDateTime.now(), endDate = LocalDateTime.now();
    private List<Part> parts = new ArrayList<Part>();
    private Appointment appointment = new Appointment(appointmentId, (Client) null, (Mechanic) null, startDate, endDate, "no comments", (Task) null);
    private TaskDto taskDto = new TaskDto(appointmentId, comments);
    private Task task = new Task(taskId, appointment, comments, parts);

    @Test
    @DisplayName("Create task - happy flow")
    void createTaskHappy() {
        // Arrange
        when(taskMapper.taskDtoToTask(taskDto)).thenReturn(task);
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);

        // Act
        Task result = taskService.createTask(taskDto);

        // Assert
        assertNotNull(result);
        assertEquals(result.getId(), appointment.getTask().getId());
    }

    @Test
    @DisplayName("Create task - appointment not found")
    void createTaskThrowAppointmentNotFound() {
        // Arrange
        when(taskMapper.taskDtoToTask(taskDto)).thenReturn(task);
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        // Act
        NotFoundException exception = assertThrows(NotFoundException.class, () -> taskService.createTask(taskDto));

        // Assert
        assertNotNull(exception);
        assertEquals(exception.getMessage(), String.format("Appointment with ID %d not found.", appointmentId));
    }

    @Test
    @DisplayName("Get all tasks - happy flow")
    void getAllTasks() {
        // Arrange
        List<Task> tasks = new ArrayList<Task>();
        tasks.add(task);

        when(taskRepository.findAll()).thenReturn(tasks);

        // Act
        List<Task> result = taskService.getAllTasks();

        // Assert
        assertNotNull(result);
        assertArrayEquals(result.toArray(), tasks.toArray());
    }
}