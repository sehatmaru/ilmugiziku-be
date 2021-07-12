package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AuthModel;
import xcode.ilmugiziku.domain.repository.AuthRepository;
import xcode.ilmugiziku.domain.request.LoginRequest;
import xcode.ilmugiziku.domain.request.RegisterRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.LoginResponse;
import xcode.ilmugiziku.presenter.AuthPresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.Utils.*;
import static xcode.ilmugiziku.shared.refs.RegistrationTypeRefs.GOOGLE;
import static xcode.ilmugiziku.shared.refs.RoleRefs.CONSUMER;

@Service
public class AuthService implements AuthPresenter {

   final AuthRepository authRepository;

   public AuthService(AuthRepository authRepository) {
      this.authRepository = authRepository;
   }

   @Override
   public BaseResponse<LoginResponse> login(LoginRequest request) {
      BaseResponse<LoginResponse> response = new BaseResponse<>();
      LoginResponse loginResponse = new LoginResponse();

      if (request.validate()) {
         AuthModel model = new AuthModel();

         try {
            model= authRepository.findByEmailAndDeletedAtIsNull(request.getEmail());
         } catch (Exception e) {
            response.setStatusCode(FAILED_CODE);
            response.setMessage(e.toString());
         }

         if (model != null) {
            if (request.getType() == GOOGLE) {
               loginResponse.setEmail(model.getEmail());
               loginResponse.setFirstName(model.getFirstName());
               loginResponse.setLastName(model.getLastName());
               loginResponse.setGender(model.getGender());
               loginResponse.setSecureId(model.getSecureId());
               loginResponse.setType(model.getType());
               loginResponse.setRole(model.getRole());

               response.setStatusCode(SUCCESS_CODE);
               response.setMessage(SUCCESS_MESSAGE);
               response.setResult(loginResponse);
            } else {
               if (model.getPassword().equals(encrypt(request.getPassword()))) {
                  loginResponse.setEmail(model.getEmail());
                  loginResponse.setFirstName(model.getFirstName());
                  loginResponse.setLastName(model.getLastName());
                  loginResponse.setGender(model.getGender());
                  loginResponse.setSecureId(model.getSecureId());
                  loginResponse.setType(model.getType());
                  loginResponse.setRole(model.getRole());

                  response.setStatusCode(SUCCESS_CODE);
                  response.setMessage(SUCCESS_MESSAGE);
                  response.setResult(loginResponse);
               } else {
                  response.setStatusCode(NOT_FOUND_CODE);
                  response.setMessage(AUTH_ERROR_MESSAGE);
               }
            }
         } else {
            response.setStatusCode(NOT_FOUND_CODE);
            response.setMessage(AUTH_ERROR_MESSAGE);
         }
      } else {
         response.setStatusCode(PARAMS_CODE);
         response.setMessage(PARAMS_ERROR_MESSAGE);
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


               AuthModel saved = authRepository.save(authModel);

               response.setStatusCode(SUCCESS_CODE);
               response.setMessage(SUCCESS_MESSAGE);
               createResponse.setSecureId(saved.getSecureId());

               response.setResult(createResponse);
            } else {
               response.setStatusCode(EXIST_CODE);
               response.setMessage(EXIST_MESSAGE);
            }
         } catch (Exception e) {
            response.setStatusCode(FAILED_CODE);
            response.setMessage(e.toString());
         }
      } else {
         response.setStatusCode(PARAMS_CODE);
         response.setMessage(PARAMS_ERROR_MESSAGE);
      }

      return response;
   }

   @Override
   public BaseResponse<List<LoginResponse>> getUserList() {
      BaseResponse<List<LoginResponse>> response = new BaseResponse<>();
      List<LoginResponse> loginResponses = new ArrayList<>();

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

         response.setStatusCode(SUCCESS_CODE);
         response.setMessage(SUCCESS_MESSAGE);
         response.setResult(loginResponses);
      } catch (Exception e) {
         response.setStatusCode(FAILED_CODE);
         response.setMessage(e.toString());
      }

      return response;
   }
}
