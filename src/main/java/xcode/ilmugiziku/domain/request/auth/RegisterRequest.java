package xcode.ilmugiziku.domain.request.auth;

import lombok.Getter;
import lombok.Setter;

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
    private int registrationType;
    @NotNull()
    private int role;

    public RegisterRequest() {
    }
}
