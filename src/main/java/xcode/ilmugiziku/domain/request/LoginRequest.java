package xcode.ilmugiziku.domain.request;

import lombok.Getter;
import lombok.Setter;

import static xcode.ilmugiziku.shared.refs.RegistrationTypeRefs.EMAIL;
import static xcode.ilmugiziku.shared.refs.RegistrationTypeRefs.GOOGLE;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
    private int type;

    public LoginRequest() {
    }

    public boolean isValid() {
        boolean result = !email.isEmpty();

        if (type != EMAIL && type != GOOGLE) {
            result = false;
        }

        if (password == null) {
            result = false;
        }

        return result;
    }
}
