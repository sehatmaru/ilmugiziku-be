package xcode.ilmugiziku.domain.request.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AdminLoginRequest {

    @NotBlank()
    private String email;
    @NotBlank()
    private String password;

    public AdminLoginRequest() {
    }

}
