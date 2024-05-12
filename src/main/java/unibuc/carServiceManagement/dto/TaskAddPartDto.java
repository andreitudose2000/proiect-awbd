package unibuc.carServiceManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskAddPartDto {
    @NotNull(message = "ID piesa este obligatoriu")
    private Long partId;
}
