package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AuthModel;
import xcode.ilmugiziku.domain.model.AuthTokenModel;
import xcode.ilmugiziku.domain.repository.AuthRepository;
import xcode.ilmugiziku.domain.request.auth.LoginRequest;
import xcode.ilmugiziku.domain.request.auth.RegisterRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.auth.LoginResponse;
import xcode.ilmugiziku.domain.response.auth.UserResponse;
import xcode.ilmugiziku.mapper.AuthMapper;
import xcode.ilmugiziku.presenter.AuthPresenter;

import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.Utils.encrypt;
import static xcode.ilmugiziku.shared.refs.RegistrationTypeRefs.GOOGLE;
import static xcode.ilmugiziku.shared.refs.RoleRefs.CONSUMER;

@Service
public class AuthService implements AuthPresenter {

   private final AuthTokenService authTokenService;

   private final AuthRepository authRepository;

   private final AuthMapper authMapper = new AuthMapper();

   public AuthService(AuthTokenService authTokenService, AuthRepository authRepository) {
      this.authTokenService = authTokenService;
      this.authRepository = authRepository;
   }

   @Override
   public BaseResponse<LoginResponse> login(LoginRequest request) {
      BaseResponse<LoginResponse> response = new BaseResponse<>();

      if (request.isValid()) {
         AuthModel model = new AuthModel();

         try {
            model = authRepository.findByEmailAndDeletedAtIsNull(request.getEmail());
         } catch (Exception e) {
            response.setFailed(e.toString());
         }

         if (model != null) {
            if (request.getType() == GOOGLE) {
               AuthTokenModel tokenModel = authTokenService.getAuthTokenByAuthSecureId(model.getSecureId());

               if (tokenModel == null) {
                  String token = authTokenService.generateAuthToken(model.getSecureId());
                  response.setSuccess(authMapper.loginModelToLoginResponse(model, token));
               } else {
                  if (authTokenService.isStillValidToken(tokenModel)) {
                     response.setExistData(AUTH_EXIST_MESSAGE);
                  } else {
                     String token = authTokenService.refreshToken(tokenModel, model.getSecureId());
                     response.setSuccess(authMapper.loginModelToLoginResponse(model, token));
                  }
               }
            } else {
               if (model.getPassword().equals(encrypt(request.getPassword()))) {
                  AuthTokenModel tokenModel = authTokenService.getAuthTokenByAuthSecureId(model.getSecureId());

                  if (tokenModel == null) {
                     String token = authTokenService.generateAuthToken(model.getSecureId());

                     response.setSuccess(authMapper.loginModelToLoginResponse(model, token));
                  } else {
                     if (authTokenService.isStillValidToken(tokenModel)) {
                        response.setExistData(AUTH_EXIST_MESSAGE);
                     } else {
                        String token = authTokenService.refreshToken(tokenModel, model.getSecureId());

                        response.setSuccess(authMapper.loginModelToLoginResponse(model, token));
                     }
                  }
               } else {
                  response.setNotFound(AUTH_ERROR_MESSAGE);
               }
            }
         } else {
            response.setNotFound(AUTH_ERROR_MESSAGE);
         }
      } else {
         response.setWrongParams();
      }

      return response;
   }

   @Override
   public BaseResponse<CreateBaseResponse> register(RegisterRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      if (request.validate()) {
         try {
            if (authRepository.findByEmailAndDeletedAtIsNull(request.getEmail()) == null) {
               if (request.getRegistrationType() != GOOGLE) {
                  if (!request.getPassword().isEmpty()) {
                     AuthModel model = authMapper.registerRequestToLoginModel(request);
                     authRepository.save(model);

                     createResponse.setSecureId(model.getSecureId());

                     response.setSuccess(createResponse);
                  } else {
                     response.setWrongParams();
                  }
               } else {
                  AuthModel model = authMapper.registerRequestToLoginModel(request);
                  authRepository.save(model);

                  createResponse.setSecureId(model.getSecureId());

                  response.setSuccess(createResponse);
               }
            } else {
               response.setExistData("");
            }
         } catch (Exception e) {
            response.setFailed(e.toString());
         }
      } else {
         response.setWrongParams();
      }

      return response;
   }

   @Override
   public BaseResponse<List<UserResponse>> getUserList(String token) {
      BaseResponse<List<UserResponse>> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         try {
            List<AuthModel> models = authRepository.findByRoleAndDeletedAtIsNull(CONSUMER);

            response.setSuccess(authMapper.loginModelsToLoginResponses(models));
         } catch (Exception e) {
            response.setFailed(e.toString());
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   @Override
   public BaseResponse<Boolean> logout(String token) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      AuthTokenModel model = authTokenService.getAuthTokenByToken(token);

      if (model != null) {
         try {
            authTokenService.destroyAuthToken(model);

            response.setSuccess(true);
         } catch (Exception e) {
            response.setFailed(e.toString());
         }
      } else {
         response.setNotFound("");
      }

      return response;
   }

   @Override
   public BaseResponse<Boolean> destroyToken(String request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (!request.isEmpty()) {
         AuthModel model = new AuthModel();

         try {
            model = authRepository.findByEmailAndDeletedAtIsNull(request);
         } catch (Exception e) {
            response.setFailed(e.toString());
         }

         if (model != null) {
            AuthTokenModel authTokenModel = authTokenService.getAuthTokenByAuthSecureId(model.getSecureId());

            if (authTokenModel != null) {
               authTokenService.destroyAuthToken(authTokenModel);

               response.setSuccess(true);
            }
         } else {
            response.setNotFound(AUTH_ERROR_MESSAGE);
         }
      } else {
         response.setWrongParams();
      }

      return response;
   }

   public AuthModel getActiveAuthBySecureId(String secureId) {
      return authRepository.findBySecureIdAndDeletedAtIsNull(secureId);
   }

   public AuthModel getAuthBySecureId(String secureId) {
      return authRepository.findBySecureId(secureId);
   }

}
