package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.AuthModel;
import xcode.ilmugiziku.domain.response.auth.LoginResponse;

public class LoginMapper {
    public LoginResponse modelToResponse(AuthModel model, String token) {
        if (model != null) {
            LoginResponse response = new LoginResponse();
            response.setSecureId(model.getSecureId());
            response.setFirstName(model.getFirstName());
            response.setLastName(model.getLastName());
            response.setRole(model.getRole());
            response.setType(model.getType());
            response.setGender(model.getGender());
            response.setEmail(model.getEmail());
            response.setToken(token);

            return response;
        } else {
            return null;
        }
    }
}
