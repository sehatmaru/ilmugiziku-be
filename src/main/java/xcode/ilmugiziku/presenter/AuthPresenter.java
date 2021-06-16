package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.model.AuthModel;
import xcode.ilmugiziku.domain.request.RegisterRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;

public interface AuthPresenter {
   AuthModel findById(int id);

   AuthModel findByEmailAndPasswordAndRole(String email, String password, int role);

   BaseResponse<CreateBaseResponse> create(RegisterRequest request);
}
