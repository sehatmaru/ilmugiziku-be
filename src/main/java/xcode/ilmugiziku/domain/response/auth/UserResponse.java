package xcode.ilmugiziku.domain.response.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private String secureId;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private int type;
    private int role;
}
