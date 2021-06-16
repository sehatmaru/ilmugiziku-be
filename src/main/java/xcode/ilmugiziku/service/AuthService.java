package xcode.ilmugiziku.service;

import xcode.ilmugiziku.domain.model.AuthModel;
import xcode.ilmugiziku.domain.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.request.RegisterRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.presenter.AuthPresenter;

import java.util.Date;

import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.Utils.encrypt;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;
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
   public AuthModel findByEmailAndPasswordAndRole(String email, String password, int role) {
      return authRepository.findByEmailAndPasswordAndRoleAndDeletedAtIsNull(email, password, role);
   }

   @Override
   public BaseResponse<CreateBaseResponse> create(RegisterRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      if (request.getRole() == ADMIN || request.getRole() == CONSUMER) {
         if (request.getType() == EMAIL || request.getType() == GOOGLE_FB) {
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

            try {
               AuthModel saved = authRepository.save(authModel);

               response.setStatusCode(SUCCESS_CODE);
               response.setMessage(SUCCESS_MESSAGE);
               createResponse.setSecureId(saved.getSecureId());

               response.setResult(createResponse);
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
