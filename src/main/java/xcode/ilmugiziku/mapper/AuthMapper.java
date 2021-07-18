package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.AuthModel;
import xcode.ilmugiziku.domain.request.RegisterRequest;
import xcode.ilmugiziku.domain.response.LoginResponse;
import xcode.ilmugiziku.domain.response.UserResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.Utils.encrypt;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class AuthMapper {
    public LoginResponse loginModelToLoginResponse(AuthModel model, String token) {
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

    public AuthModel registerRequestToLoginModel(RegisterRequest request) {
        if (request != null) {
            AuthModel response = new AuthModel();
            response.setSecureId(generateSecureId());
            response.setFirstName(request.getFirstName());
            response.setLastName(request.getLastName());
            response.setGender(request.getGender());
            response.setEmail(request.getEmail());
            response.setType(request.getRegistrationType());
            response.setRole(request.getRole());
            response.setCreatedAt(new Date());

            if (!request.getPassword().isEmpty()) {
                response.setPassword(encrypt(request.getPassword()));
            }

            return response;
        } else {
            return null;
        }
    }

    public List<UserResponse> loginModelsToLoginResponses(List<AuthModel> models) {
        if (models != null) {
            List<UserResponse> responses = new ArrayList<>();

            for (AuthModel model : models) {
                UserResponse response = new UserResponse();
                response.setSecureId(model.getSecureId());
                response.setFirstName(model.getFirstName());
                response.setLastName(model.getLastName());
                response.setEmail(model.getEmail());
                response.setGender(model.getGender());
                response.setType(model.getType());
                response.setRole(model.getRole());

                responses.add(response);
            }

            return responses;
        } else {
            return null;
        }
    }
}
