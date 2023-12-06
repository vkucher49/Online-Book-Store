package mate.academy.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    @Size(min = 4, max = 50)
    private String email;
    @NotBlank
    @Size(min = 4, max = 50)
    private String password;
    @NotBlank
    @Size(min = 4, max = 50)
    private String repeatPassword;
    @NotNull
    @Size(min = 4, max = 50)
    private String firstName;
    @NotNull
    @Size(min = 4, max = 50)
    private String lastName;
    private String shippingAddress;
}
