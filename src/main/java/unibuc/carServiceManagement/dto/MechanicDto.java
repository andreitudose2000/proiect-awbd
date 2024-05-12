package unibuc.carServiceManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import unibuc.carServiceManagement.domain.Speciality;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MechanicDto {
    @NotBlank(message = "Prenumele este obligatoriu")
    private String firstName;
    @NotBlank(message = "Prenumele este obligatoriu")
    private String lastName;
    @NotNull(message = "Specialitatea este obligatorie")
    private Speciality speciality;

    private Long carServiceId;

    public MechanicDto(String firstName, String lastName, Speciality speciality) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.speciality = speciality;
    }

}
