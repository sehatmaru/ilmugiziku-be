package xcode.ilmugiziku.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private int type;
    private int role;

    public RegisterRequest() {
    }
}
