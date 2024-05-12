package unibuc.carServiceManagement.dto;

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
    @NotNull(message = "ID programare este obligatoriu")
    private Long appointmentId;
    @Size(max = 500, message = "Lungimea textului nu trebuie sa depaseasca 500 caractere")
    private String comments;

}
