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
public class ClinicDto {
    @NotBlank(message = "Name must be provided")
    private String name;
    @NotBlank(message = "Address must be provided")
    private String address;
}
