package xcode.marsiajar.mapper;

import org.springframework.beans.BeanUtils;
import xcode.marsiajar.domain.enums.RegistrationTypeEnum;
import xcode.marsiajar.domain.enums.RoleEnum;
import xcode.marsiajar.domain.model.ProfileModel;
import xcode.marsiajar.domain.model.UserModel;
import xcode.marsiajar.domain.request.user.AddUpdateAdminRequest;
import xcode.marsiajar.domain.request.user.RegisterRequest;
import xcode.marsiajar.domain.response.user.LoginResponse;
import xcode.marsiajar.domain.response.user.UserResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static xcode.marsiajar.shared.Utils.encrypt;
import static xcode.marsiajar.shared.Utils.generateSecureId;

public class UserMapper {

    private static final String DEFAULT_ADMIN_PASSWORD = "admin123";
    public LoginResponse loginModelToLoginResponse(UserModel userModel, ProfileModel profileModel, String token) {
        if (userModel != null && profileModel != null) {
            LoginResponse response = new LoginResponse();
            BeanUtils.copyProperties(userModel, response);
            BeanUtils.copyProperties(profileModel, response);
            response.setAccessToken(token);

            return response;
        } else {
            return null;
        }
    }

    public UserModel registerRequestToLoginModel(RegisterRequest request) {
        if (request != null) {
            UserModel response = new UserModel();
            BeanUtils.copyProperties(request, response);
            response.setSecureId(generateSecureId());
            response.setType(RegistrationTypeEnum.EMAIL);
            response.setRole(RoleEnum.CONSUMER);
            response.setCreatedAt(new Date());
            response.setActive(true);

            if (!request.getPassword().isEmpty()) {
                response.setPassword(encrypt(request.getPassword()));
            }

            return response;
        } else {
            return null;
        }
    }

    public UserModel adminRequestToUserModel(AddUpdateAdminRequest request) {
        if (request != null) {
            UserModel response = new UserModel();
            BeanUtils.copyProperties(request, response);
            response.setSecureId(generateSecureId());
            response.setType(RegistrationTypeEnum.EMAIL);
            response.setEmail(request.getEmail());
            response.setRole(RoleEnum.ADMIN);
            response.setCreatedAt(new Date());
            response.setActive(true);
            response.setPassword(encrypt(DEFAULT_ADMIN_PASSWORD));

            return response;
        } else {
            return null;
        }
    }

    public ProfileModel registerRequestToProfileModel(RegisterRequest request, String userId) {
        if (request != null) {
            ProfileModel response = new ProfileModel();
            BeanUtils.copyProperties(request, response);
            response.setSecureId(generateSecureId());
            response.setUser(userId);
            response.setCreatedAt(new Date());

            return response;
        } else {
            return null;
        }
    }

    public ProfileModel adminRequestToProfileModel(AddUpdateAdminRequest request, String userId) {
        if (request != null) {
            ProfileModel response = new ProfileModel();
            BeanUtils.copyProperties(request, response);
            response.setSecureId(generateSecureId());
            response.setUser(userId);
            response.setCreatedAt(new Date());

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
                BeanUtils.copyProperties(model, response);

                responses.add(response);
            }

            return responses;
        } else {
            return Collections.emptyList();
        }
    }
}
