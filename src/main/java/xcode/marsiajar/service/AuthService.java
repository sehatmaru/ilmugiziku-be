package xcode.marsiajar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.marsiajar.domain.dto.CurrentUser;
import xcode.marsiajar.domain.model.TokenModel;
import xcode.marsiajar.domain.model.UserModel;
import xcode.marsiajar.domain.repository.ProfileRepository;
import xcode.marsiajar.domain.repository.TokenRepository;
import xcode.marsiajar.domain.repository.UserRepository;
import xcode.marsiajar.domain.request.user.AddUpdateAdminRequest;
import xcode.marsiajar.domain.request.user.AdminLoginRequest;
import xcode.marsiajar.domain.request.user.LoginRequest;
import xcode.marsiajar.domain.request.user.RegisterRequest;
import xcode.marsiajar.domain.response.BaseResponse;
import xcode.marsiajar.domain.response.CreateBaseResponse;
import xcode.marsiajar.domain.response.user.LoginResponse;
import xcode.marsiajar.exception.AppException;
import xcode.marsiajar.mapper.UserMapper;

import static xcode.marsiajar.domain.enums.RegistrationTypeEnum.GOOGLE;
import static xcode.marsiajar.shared.ResponseCode.*;
import static xcode.marsiajar.shared.Utils.encrypt;

@Service
public class AuthService {

   @Autowired private JwtService jwtService;
   @Autowired private UserRepository userRepository;
   @Autowired private TokenRepository tokenRepository;
   @Autowired private ProfileRepository profileRepository;

   private final UserMapper userMapper = new UserMapper();

   /**
    * login consumer by email/google account
    * @param request body
    * @return response
    */
   public BaseResponse<LoginResponse> loginConsumer(LoginRequest request) {
      BaseResponse<LoginResponse> response = new BaseResponse<>();

      UserModel model = userRepository.getActiveConsumerByEmail(request.getEmail());

      if (model == null) {
         throw new AppException(AUTH_ERROR_MESSAGE);
      }

      if (request.getType() == GOOGLE) {
         response.setSuccess(getGoogleAccount(model));
      } else {
         if (model.getPassword().equals(encrypt(request.getPassword()))) {
            response = getEmailAccount(model);
         } else {
            response.setNotFound(AUTH_ERROR_MESSAGE);
         }
      }

      return response;
   }

   /**
    * login admin
    * @param request body
    * @return response
    */
   public BaseResponse<LoginResponse> loginAdmin(AdminLoginRequest request) {
      BaseResponse<LoginResponse> response = new BaseResponse<>();

      UserModel model = userRepository.getActiveAdmin(request.getEmail());

      if (model == null || !model.getPassword().equals(encrypt(request.getPassword()))) {
         throw new AppException(AUTH_ERROR_MESSAGE);
      }

      try {
         response.setSuccess(userMapper.loginModelToLoginResponse(model, profileRepository.getProfileBySecureId(model.getSecureId()), saveToken(model)));
      } catch (Exception e) {
         throw new AppException(e.toString());
      }

      return response;
   }

   private String saveToken(UserModel model) {
      String token = jwtService.generateToken(model);
      tokenRepository.save(new TokenModel(
              token,
              model.getSecureId(),
              false,
              model.getRole()
      ));

      return token;
   }

   private BaseResponse<LoginResponse> getEmailAccount(UserModel model) {
      BaseResponse<LoginResponse> response = new BaseResponse<>();

      TokenModel tokenModel = tokenRepository.getTokenByUser(model.getSecureId());

      if (tokenModel != null) {
         throw new AppException(LOGIN_EXIST_MESSAGE);
      }

      response.setSuccess(userMapper.loginModelToLoginResponse(model, profileRepository.getProfileBySecureId(model.getSecureId()), saveToken(model)));
      
      return response;
   }

   private LoginResponse getGoogleAccount(UserModel model) {
      LoginResponse response;
      TokenModel tokenModel = tokenRepository.getTokenByUser(model.getSecureId());

      if (tokenModel == null) {
         response = userMapper.loginModelToLoginResponse(model, profileRepository.getProfileBySecureId(model.getSecureId()), saveToken(model));
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

   public BaseResponse<CreateBaseResponse> createAdmin(AddUpdateAdminRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      try {
         if (userRepository.findByEmailAndDeletedAtIsNull(request.getEmail()) == null) {
            UserModel model = userMapper.adminRequestToUserModel(request);
            model.setCreatedBy(CurrentUser.get().getUserSecureId());

            userRepository.save(model);
            profileRepository.save(userMapper.adminRequestToProfileModel(request, model.getSecureId()));

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

}
