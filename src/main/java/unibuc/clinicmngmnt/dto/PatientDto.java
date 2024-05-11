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
public class PatientDto {
    @NotBlank(message = "First name must be provided")
    private String firstName;
    @NotBlank(message = "Last name must be provided")
    private String lastName;
    @Pattern(regexp = PHONE_NO, message = "Phone number must be provided and match one of the following formats: ###-###-####, " +
            "(###) ###-####, " +
            "### ### ####, " +
            "###.###.####")
    private String phone;
    private String email;
    @NotNull(message = "Date of birth must be provided")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
}
