package unibuc.carServiceManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarServiceDto {
    @NotBlank(message = "Numele este obligatoriu")
    private String name;
    @NotBlank(message = "Adresa este obligatorie")
    private String address;
}
