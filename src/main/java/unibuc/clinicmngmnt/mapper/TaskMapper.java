package unibuc.clinicmngmnt.mapper;

import org.springframework.stereotype.Component;
import unibuc.clinicmngmnt.dto.TaskDto;
import unibuc.clinicmngmnt.domain.Task;

@Component
public class TaskMapper {
    public Task taskDtoToTask(TaskDto taskDto) {
        return new Task(taskDto.getComments());
    }
}
