package xcode.ilmugiziku.service;

import xcode.ilmugiziku.domain.model.AuthModel;
import xcode.ilmugiziku.domain.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.request.RegisterRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.LoginResponse;
import xcode.ilmugiziku.presenter.AuthPresenter;

import java.util.Date;

import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.Utils.*;
import static xcode.ilmugiziku.shared.refs.RoleRefs.ADMIN;
import static xcode.ilmugiziku.shared.refs.RoleRefs.CONSUMER;
import static xcode.ilmugiziku.shared.refs.TypeRefs.EMAIL;
import static xcode.ilmugiziku.shared.refs.TypeRefs.GOOGLE_FB;

@Service
public class AuthService implements AuthPresenter {

   @Autowired
   AuthRepository authRepository;

   @Override
   public AuthModel findById(int id) {
      return authRepository.findById(id);
   }

   @Override
   public BaseResponse<LoginResponse> login(String email, String password, int role, int type) {
      BaseResponse<LoginResponse> response = new BaseResponse<>();
      LoginResponse loginResponse = new LoginResponse();

      AuthModel model = new AuthModel();

      try {
          model= authRepository.findByEmailAndRole(email, role);
      } catch (Exception e) {
         response.setStatusCode(FAILED_CODE);
         response.setMessage(e.toString());
      }

      if (role == ADMIN || role == CONSUMER) {
         if (type == EMAIL || type == GOOGLE_FB) {
            if (model != null) {
               if (password.equals(decrypt(model.getPassword()))) {
                  loginResponse.setEmail(model.getEmail());
                  loginResponse.setName(model.getName());
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
            } else {
               response.setStatusCode(NOT_FOUND_CODE);
               response.setMessage(AUTH_ERROR_MESSAGE);
            }
         } else {
            response.setStatusCode(PARAMS_CODE);
            response.setMessage(PARAMS_ERROR_MESSAGE);
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

      if (request.getRole() == ADMIN || request.getRole() == CONSUMER) {
         if (request.getType() == EMAIL || request.getType() == GOOGLE_FB) {
            try {
               if (authRepository.findByEmail(request.getEmail()) == null) {
                  AuthModel authModel = new AuthModel();
                  authModel.setSecureId(generateSecureId());
                  authModel.setName(request.getName());
                  authModel.setEmail(request.getEmail());
                  authModel.setType(request.getType());
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
      } else {
         response.setStatusCode(PARAMS_CODE);
         response.setMessage(PARAMS_ERROR_MESSAGE);
      }

      return response;
   }
}
