package unibuc.clinicmngmnt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicationDto {
    @NotBlank(message = "Name must be provided")
    private String name;
    @NotBlank(message = "Brand must be provided")
    private String brand;
}
