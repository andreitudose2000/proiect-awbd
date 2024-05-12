package unibuc.carServiceManagement.mapper;

import org.springframework.stereotype.Component;
import unibuc.carServiceManagement.dto.TaskDto;
import unibuc.carServiceManagement.domain.Task;

@Component
public class TaskMapper {
    public Task taskDtoToTask(TaskDto taskDto) {
        return new Task(taskDto.getComments());
    }
}
