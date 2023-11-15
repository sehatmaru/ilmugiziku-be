package xcode.marsiajar.domain.response.user;

import lombok.Getter;
import lombok.Setter;
import xcode.marsiajar.domain.enums.RegistrationTypeEnum;
import xcode.marsiajar.domain.enums.RoleEnum;

import java.util.Date;

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
    private Date createdAt;
}
