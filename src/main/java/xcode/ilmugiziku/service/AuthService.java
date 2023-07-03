package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AuthModel;
import xcode.ilmugiziku.domain.model.AuthTokenModel;
import xcode.ilmugiziku.domain.model.PaymentModel;
import xcode.ilmugiziku.domain.repository.AuthRepository;
import xcode.ilmugiziku.domain.request.auth.LoginRequest;
import xcode.ilmugiziku.domain.request.auth.RegisterRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.auth.LoginResponse;
import xcode.ilmugiziku.domain.response.auth.UserResponse;
import xcode.ilmugiziku.mapper.AuthMapper;

import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.Utils.encrypt;
import static xcode.ilmugiziku.shared.refs.PackageTypeRefs.*;
import static xcode.ilmugiziku.shared.refs.RegistrationTypeRefs.GOOGLE;
import static xcode.ilmugiziku.shared.refs.RoleRefs.ADMIN;
import static xcode.ilmugiziku.shared.refs.RoleRefs.CONSUMER;

@Service
public class AuthService {

   @Autowired private AuthTokenService authTokenService;
   @Autowired @Lazy private PaymentService paymentService;
   @Autowired private AuthRepository authRepository;

   private final AuthMapper authMapper = new AuthMapper();

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

               if (model.isPremium()) {
                  refreshPremiumPackage(model);
               }

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
               if (model.isPremium()) {
                  refreshPremiumPackage(model);
               }

               if (model.getPassword().equals(encrypt(request.getPassword()))) {
                  if (model.getRole() == ADMIN) {
                     String token = authTokenService.generateAuthToken(model.getSecureId());

                     response.setSuccess(authMapper.loginModelToLoginResponse(model, token));
                  } else {
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

   public boolean isRoleAdmin(String secureId) {
      return authRepository.findBySecureIdAndDeletedAtIsNull(secureId).getRole() == ADMIN;
   }

   public void saveAuthModel(AuthModel model) {
      authRepository.save(model);
   }

   public void refreshPremiumPackage(AuthModel authModel) {
      if (authModel.isSKBPackage()) {
         PaymentModel paymentModel;

         if (authModel.isSKBExpert()) {
            paymentModel = paymentService.getPaidPaymentByAuthSecureIdAndType(authModel.getSecureId(), SKB_EXPERT);
         } else {
            paymentModel = paymentService.getPaidPaymentByAuthSecureIdAndType(authModel.getSecureId(), SKB_NEWBIE);
         }

         if (paymentModel.getExpiredDate().before(new Date())) {
            paymentModel.setDeletedAt(new Date());
            authModel.setPackages(authModel.getPackages().replace(String.valueOf(paymentModel.getPackageType()), ""));

            paymentService.savePaymentModel(paymentModel);
            authRepository.save(authModel);
         }
      }

      if (authModel.isUKOMPackage()) {
         PaymentModel paymentModel;

         if (authModel.isUKOMExpert()) {
            paymentModel = paymentService.getPaidPaymentByAuthSecureIdAndType(authModel.getSecureId(), UKOM_EXPERT);
         } else {
            paymentModel = paymentService.getPaidPaymentByAuthSecureIdAndType(authModel.getSecureId(), UKOM_NEWBIE);
         }

         if (paymentModel.getExpiredDate().before(new Date())) {
            paymentModel.setDeletedAt(new Date());
            authModel.setPackages(authModel.getPackages().replace(String.valueOf(paymentModel.getPackageType()), ""));

            paymentService.savePaymentModel(paymentModel);
            authRepository.save(authModel);
         }
      }
   }
}
