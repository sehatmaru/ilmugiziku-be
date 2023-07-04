package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.dto.CurrentUser;
import xcode.ilmugiziku.domain.model.TokenModel;
import xcode.ilmugiziku.domain.model.UserModel;
import xcode.ilmugiziku.domain.repository.ProfileRepository;
import xcode.ilmugiziku.domain.repository.TokenRepository;
import xcode.ilmugiziku.domain.repository.UserRepository;
import xcode.ilmugiziku.domain.request.user.LoginRequest;
import xcode.ilmugiziku.domain.request.user.RegisterRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.user.LoginResponse;
import xcode.ilmugiziku.domain.response.user.UserResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.UserMapper;

import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.Utils.encrypt;
import static xcode.ilmugiziku.shared.refs.RegistrationTypeRefs.GOOGLE;
import static xcode.ilmugiziku.shared.refs.RoleRefs.ADMIN;
import static xcode.ilmugiziku.shared.refs.RoleRefs.CONSUMER;

@Service
public class UserService {

   @Autowired private JwtService jwtService;
   @Autowired private BimbelService bimbelService;
   @Autowired private UserRepository userRepository;
   @Autowired private TokenRepository tokenRepository;
   @Autowired private ProfileRepository profileRepository;

   private final UserMapper userMapper = new UserMapper();

   public BaseResponse<LoginResponse> login(LoginRequest request) {
      BaseResponse<LoginResponse> response = new BaseResponse<>();

      UserModel model = userRepository.findByEmailAndDeletedAtIsNull(request.getEmail());

      if (model == null) {
         throw new AppException(AUTH_ERROR_MESSAGE);
      }

      if (request.getType() == GOOGLE) {
         response.setSuccess(getGoogleAccount(model));
      } else {
         if (model.isPremium()) bimbelService.refreshPremiumPackage(model);

         if (model.getPassword().equals(encrypt(request.getPassword()))) {
            response = getEmailAccount(model);
         } else {
            response.setNotFound(AUTH_ERROR_MESSAGE);
         }
      }

      return response;
   }

   private String saveToken(UserModel model) {
      String token = jwtService.generateToken(model);
      tokenRepository.save(new TokenModel(
              token,
              model.getSecureId(),
              false
      ));

      return token;
   }

   private BaseResponse<LoginResponse> getEmailAccount(UserModel model) {
      BaseResponse<LoginResponse> response = new BaseResponse<>();
      
      String token = saveToken(model);

      if (model.getRole() != ADMIN) {
         TokenModel tokenModel = tokenRepository.getTokenByUser(model.getSecureId());

         if (tokenModel != null) {
            throw new AppException(EXIST_MESSAGE);
         }
      }

      response.setSuccess(userMapper.loginModelToLoginResponse(model, profileRepository.getProfileBySecureId(model.getSecureId()).orElse(null), token));
      
      return response;
   }

   private LoginResponse getGoogleAccount(UserModel model) {
      LoginResponse response;
      TokenModel tokenModel = tokenRepository.getTokenByUser(model.getSecureId());

      if (model.isPremium()) {
         bimbelService.refreshPremiumPackage(model);
      }

      if (tokenModel == null) {
         response = userMapper.loginModelToLoginResponse(model, profileRepository.getProfileBySecureId(model.getSecureId()).orElse(null), saveToken(model));
      } else {
         throw new AppException(AUTH_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<CreateBaseResponse> register(RegisterRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      try {
         if (userRepository.findByEmailAndDeletedAtIsNull(request.getEmail()) == null) {
            UserModel model = userMapper.registerRequestToLoginModel(request);
            userRepository.save(model);
            profileRepository.save(userMapper.registerRequestToProfileModel(request, model.getSecureId()));

            createResponse.setSecureId(model.getSecureId());
            response.setSuccess(createResponse);
         } else {
            throw new AppException(EMAIL_EXIST);
         }
      } catch (Exception e) {
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<List<UserResponse>> getUserList() {
      BaseResponse<List<UserResponse>> response = new BaseResponse<>();

      try {
         List<UserModel> models = userRepository.findByRoleAndDeletedAtIsNull(CONSUMER);

         response.setSuccess(userMapper.loginModelsToLoginResponses(models));
      } catch (Exception e) {
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> logout() {
      BaseResponse<Boolean> response = new BaseResponse<>();

      TokenModel model = tokenRepository.getToken(CurrentUser.get().getToken());

      if (model != null) {
         try {
            tokenRepository.delete(model);

            response.setSuccess(true);
         } catch (Exception e) {
            throw new AppException(e.toString());
         }
      } else {
         throw new AppException(NOT_FOUND_MESSAGE);
      }

      return response;
   }

   public boolean isRoleAdmin(String secureId) {
      return userRepository.findBySecureIdAndDeletedAtIsNull(secureId).getRole() == ADMIN;
   }

}