package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.ProfileModel;
import xcode.ilmugiziku.domain.model.UserModel;
import xcode.ilmugiziku.domain.request.user.RegisterRequest;
import xcode.ilmugiziku.domain.response.user.LoginResponse;
import xcode.ilmugiziku.domain.response.user.UserResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.Utils.encrypt;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class UserMapper {
    public LoginResponse loginModelToLoginResponse(UserModel userModel, ProfileModel profileModel, String token) {
        if (userModel != null && profileModel != null) {
            LoginResponse response = new LoginResponse();
            response.setSecureId(userModel.getSecureId());
            response.setFirstName(profileModel.getFirstName());
            response.setLastName(profileModel.getLastName());
            response.setRole(userModel.getRole());
            response.setType(userModel.getType());
            response.setGender(profileModel.getGender());
            response.setEmail(profileModel.getEmail());
            response.setAccessToken(token);
            response.setPremium(userModel.isPremium());

            return response;
        } else {
            return null;
        }
    }

    public UserModel registerRequestToLoginModel(RegisterRequest request) {
        if (request != null) {
            UserModel response = new UserModel();
            response.setSecureId(generateSecureId());
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

    public List<UserResponse> loginModelsToLoginResponses(List<UserModel> models) {
        if (models != null) {
            List<UserResponse> responses = new ArrayList<>();

            for (UserModel model : models) {
                UserResponse response = new UserResponse();
                response.setSecureId(model.getSecureId());
                response.setEmail(model.getEmail());
                response.setType(model.getType());
                response.setRole(model.getRole());

                responses.add(response);
            }

            return responses;
        } else {
            return Collections.emptyList();
        }
    }
}
