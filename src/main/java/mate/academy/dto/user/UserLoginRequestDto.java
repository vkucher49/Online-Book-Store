package mate.academy.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @NotEmpty
    @Size(min = 4, max = 50)
    @Email
    private String email;

    @NotEmpty
    @Size(min = 4, max = 50)
    private String password;
}
