package xcode.marsiajar.domain.response.user;

import lombok.Getter;
import lombok.Setter;
import xcode.marsiajar.domain.enums.RegistrationTypeEnum;
import xcode.marsiajar.domain.enums.RoleEnum;

@Getter
@Setter
public class LoginResponse {
    private String secureId;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String accessToken;
    private RegistrationTypeEnum type;
    private RoleEnum role;
}
