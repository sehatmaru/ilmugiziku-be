package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AuthModel;
import xcode.ilmugiziku.domain.model.AuthTokenModel;
import xcode.ilmugiziku.domain.repository.AuthRepository;
import xcode.ilmugiziku.domain.repository.AuthTokenRepository;
import xcode.ilmugiziku.domain.request.auth.LoginRequest;
import xcode.ilmugiziku.domain.request.auth.RegisterRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.auth.LoginResponse;
import xcode.ilmugiziku.domain.response.auth.UserResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.AuthMapper;

import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.Utils.encrypt;
import static xcode.ilmugiziku.shared.refs.RegistrationTypeRefs.GOOGLE;
import static xcode.ilmugiziku.shared.refs.RoleRefs.ADMIN;
import static xcode.ilmugiziku.shared.refs.RoleRefs.CONSUMER;

@Service
public class AuthService {

   @Autowired private AuthTokenService authTokenService;
   @Autowired private BimbelService bimbelService;
   @Autowired private AuthRepository authRepository;
   @Autowired private AuthTokenRepository authTokenRepository;

   private final AuthMapper authMapper = new AuthMapper();

   public BaseResponse<LoginResponse> login(LoginRequest request) {
      BaseResponse<LoginResponse> response = new BaseResponse<>();

      AuthModel model = authRepository.findByEmailAndDeletedAtIsNull(request.getEmail());

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

   private BaseResponse<LoginResponse> getEmailAccount(AuthModel model) {
      BaseResponse<LoginResponse> response = new BaseResponse<>();
      
      String token = authTokenService.generateAuthToken(model.getSecureId());
      if (model.getRole() != ADMIN) {
         AuthTokenModel tokenModel = authTokenRepository.findByAuthSecureId(model.getSecureId());
         if (tokenModel != null && authTokenService.isStillValidToken(tokenModel)) {
            throw new AppException(EXIST_MESSAGE);
         }
      }

      response.setSuccess(authMapper.loginModelToLoginResponse(model, token));
      
      return response;
   }

   private LoginResponse getGoogleAccount(AuthModel model) {
      LoginResponse response;
      AuthTokenModel tokenModel = authTokenRepository.findByAuthSecureId(model.getSecureId());

      if (model.isPremium()) {
         bimbelService.refreshPremiumPackage(model);
      }

      if (tokenModel == null || !authTokenService.isStillValidToken(tokenModel)) {
         String token = authTokenService.generateAuthToken(model.getSecureId());
         response = authMapper.loginModelToLoginResponse(model, token);
      } else {
         throw new AppException(AUTH_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<CreateBaseResponse> register(RegisterRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      try {
         if (authRepository.findByEmailAndDeletedAtIsNull(request.getEmail()) == null) {
            AuthModel model = authMapper.registerRequestToLoginModel(request);
            authRepository.save(model);

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

   public BaseResponse<List<UserResponse>> getUserList(String token) {
      BaseResponse<List<UserResponse>> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         try {
            List<AuthModel> models = authRepository.findByRoleAndDeletedAtIsNull(CONSUMER);

            response.setSuccess(authMapper.loginModelsToLoginResponses(models));
         } catch (Exception e) {
            throw new AppException(e.toString());
         }
      } else {
         throw new AppException(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<Boolean> logout(String token) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      AuthTokenModel model = authTokenService.getAuthTokenByToken(token);

      if (model != null) {
         try {
            authTokenService.destroyAuthToken(model);

            response.setSuccess(true);
         } catch (Exception e) {
            throw new AppException(e.toString());
         }
      } else {
         throw new AppException(NOT_FOUND_MESSAGE);
      }

      return response;
   }

   public BaseResponse<Boolean> destroyToken(String request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      AuthModel model = authRepository.findByEmailAndDeletedAtIsNull(request);

      if (model != null) {
         AuthTokenModel authTokenModel = authTokenRepository.findByAuthSecureId(model.getSecureId());

         if (authTokenModel != null) {
            authTokenService.destroyAuthToken(authTokenModel);

            response.setSuccess(true);
         }
      } else {
         throw new AppException(AUTH_ERROR_MESSAGE);
      }

      return response;
   }

   public boolean isRoleAdmin(String secureId) {
      return authRepository.findBySecureIdAndDeletedAtIsNull(secureId).getRole() == ADMIN;
   }

}
