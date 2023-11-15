package xcode.marsiajar.domain.request.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank()
    private String firstName;
    @NotBlank()
    private String lastName;
    @NotBlank()
    private String email;
    @NotBlank()
    private String password;

    public RegisterRequest() {
    }
}
