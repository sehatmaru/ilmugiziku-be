package xcode.ilmugiziku.domain.request.auth;

import lombok.Getter;
import lombok.Setter;

import static xcode.ilmugiziku.shared.refs.RegistrationTypeRefs.*;
import static xcode.ilmugiziku.shared.refs.RoleRefs.ADMIN;
import static xcode.ilmugiziku.shared.refs.RoleRefs.CONSUMER;

@Getter
@Setter
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String password;
    private int registrationType;
    private int role;

    public RegisterRequest() {
    }

    public boolean validate() {
        boolean result = role == ADMIN || role == CONSUMER;

        if (registrationType != EMAIL && registrationType != GOOGLE) {
            result = false;
        }

        return result;
    }
}
