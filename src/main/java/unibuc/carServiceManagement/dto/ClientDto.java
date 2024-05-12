package unibuc.carServiceManagement.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static unibuc.carServiceManagement.domain.Pattern.PHONE_NO;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDto {
    @NotBlank(message = "Prenumele este obligatoriu")
    private String firstName;
    @NotBlank(message = "Numele este obligatoriu")
    private String lastName;
    @Pattern(regexp = PHONE_NO, message = "Numarul de telefon trebuie sa aiba urmatorul format: (+4)0xxx xxx xxx")
    private String phone;
    private String email;
}
