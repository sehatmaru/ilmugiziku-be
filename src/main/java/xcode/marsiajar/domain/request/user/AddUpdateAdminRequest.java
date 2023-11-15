package xcode.marsiajar.domain.request.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AddUpdateAdminRequest {

    @NotBlank()
    private String firstName;
    @NotBlank()
    private String lastName;
    @NotBlank()
    private String email;
    
    public AddUpdateAdminRequest() {
    }
}
