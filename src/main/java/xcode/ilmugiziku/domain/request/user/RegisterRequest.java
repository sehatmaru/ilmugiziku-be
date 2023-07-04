package xcode.ilmugiziku.domain.request.user;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.RegistrationTypeEnum;
import xcode.ilmugiziku.domain.enums.RoleEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank()
    private String firstName;
    @NotBlank()
    private String lastName;
    @NotBlank()
    private String gender;
    @NotBlank()
    private String email;
    @NotBlank()
    private String password;
    @NotNull()
    private RegistrationTypeEnum registrationType;
    @NotNull()
    private RoleEnum role;

    public RegisterRequest() {
    }
}
