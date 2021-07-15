package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AuthModel;
import xcode.ilmugiziku.domain.model.AuthTokenModel;
import xcode.ilmugiziku.domain.repository.AuthRepository;
import xcode.ilmugiziku.domain.request.LoginRequest;
import xcode.ilmugiziku.domain.request.RegisterRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.LoginResponse;
import xcode.ilmugiziku.mapper.LoginMapper;
import xcode.ilmugiziku.presenter.AuthPresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.Utils.encrypt;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;
import static xcode.ilmugiziku.shared.refs.RegistrationTypeRefs.GOOGLE;
import static xcode.ilmugiziku.shared.refs.RoleRefs.CONSUMER;

@Service
public class AuthService implements AuthPresenter {

   private final AuthTokenService authTokenService;

   private final AuthRepository authRepository;

   private final LoginMapper loginMapper = new LoginMapper();

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
                  response.setSuccess(loginMapper.modelToResponse(model, token));
               } else {
                  if (authTokenService.isStillValidToken(tokenModel)) {
                     response.setExistData(AUTH_EXIST_MESSAGE);
                  } else {
                     String token = authTokenService.refreshToken(tokenModel, model.getSecureId());
                     response.setSuccess(loginMapper.modelToResponse(model, token));
                  }
               }
            } else {
               if (model.getPassword().equals(encrypt(request.getPassword()))) {
                  AuthTokenModel tokenModel = authTokenService.getAuthTokenByAuthSecureId(model.getSecureId());

                  if (tokenModel == null) {
                     String token = authTokenService.generateAuthToken(model.getSecureId());

                     response.setSuccess(loginMapper.modelToResponse(model, token));
                  } else {
                     if (authTokenService.isStillValidToken(tokenModel)) {
                        response.setExistData(AUTH_EXIST_MESSAGE);
                     } else {
                        String token = authTokenService.refreshToken(tokenModel, model.getSecureId());

                        response.setSuccess(loginMapper.modelToResponse(model, token));
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
               AuthModel authModel = new AuthModel();
               authModel.setSecureId(generateSecureId());
               authModel.setFirstName(request.getFirstName());
               authModel.setLastName(request.getLastName());
               authModel.setGender(request.getGender());
               authModel.setEmail(request.getEmail());
               authModel.setType(request.getRegistrationType());
               authModel.setRole(request.getRole());
               authModel.setCreatedAt(new Date());

               if (request.getPassword() != null) {
                  authModel.setPassword(encrypt(request.getPassword()));
               }

               authRepository.save(authModel);

               createResponse.setSecureId(authModel.getSecureId());

               response.setSuccess(createResponse);
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
   public BaseResponse<List<LoginResponse>> getUserList(String token) {
      BaseResponse<List<LoginResponse>> response = new BaseResponse<>();
      List<LoginResponse> loginResponses = new ArrayList<>();

      if (authTokenService.isValidToken(token)) {
         try {
            List<AuthModel> models = authRepository.findByRoleAndDeletedAtIsNull(CONSUMER);

            for (AuthModel model : models) {
               LoginResponse value = new LoginResponse();
               value.setSecureId(model.getSecureId());
               value.setFirstName(model.getFirstName());
               value.setLastName(model.getLastName());
               value.setEmail(model.getEmail());
               value.setGender(model.getGender());
               value.setType(model.getType());
               value.setRole(model.getRole());

               loginResponses.add(value);
            }

            response.setSuccess(loginResponses);
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
}
