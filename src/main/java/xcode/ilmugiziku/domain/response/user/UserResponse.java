package xcode.ilmugiziku.domain.response.user;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.RegistrationTypeEnum;
import xcode.ilmugiziku.domain.enums.RoleEnum;

@Getter
@Setter
public class UserResponse {
    private String secureId;
    private String name;
    private String gender;
    private String email;
    private String token;
    private RegistrationTypeEnum type;
    private RoleEnum role;
    private boolean active;
}
