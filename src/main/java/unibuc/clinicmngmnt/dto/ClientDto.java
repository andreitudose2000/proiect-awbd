package unibuc.clinicmngmnt.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

import static unibuc.clinicmngmnt.domain.Pattern.PHONE_NO;

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
