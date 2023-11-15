package xcode.marsiajar.domain.request.user;

import lombok.Getter;
import lombok.Setter;
import xcode.marsiajar.domain.enums.RegistrationTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LoginRequest {

    @NotBlank()
    private String email;
    @NotBlank()
    private String password;
    @NotNull()
    private RegistrationTypeEnum type;

    public LoginRequest() {
    }

}
