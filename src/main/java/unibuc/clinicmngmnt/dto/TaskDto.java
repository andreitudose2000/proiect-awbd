package unibuc.clinicmngmnt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto {
    @NotNull(message = "Appointment ID must be provided")
    private Long appointmentId;
    @Size(max = 500, message = "Text length must not be greater than 500 characters")
    private String comments;

}
