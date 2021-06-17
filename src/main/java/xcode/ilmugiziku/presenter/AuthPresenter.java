package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.model.AuthModel;
import xcode.ilmugiziku.domain.request.RegisterRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.LoginResponse;

public interface AuthPresenter {
   BaseResponse<LoginResponse> login(String email, String password, int role);

   BaseResponse<CreateBaseResponse> register(RegisterRequest request);
}
