package xcode.ilmugiziku.domain.request.auth;

import lombok.Getter;
import lombok.Setter;

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
    private int type;

    public LoginRequest() {
    }

}
