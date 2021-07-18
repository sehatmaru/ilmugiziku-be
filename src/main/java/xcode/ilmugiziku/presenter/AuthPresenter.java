package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.LoginRequest;
import xcode.ilmugiziku.domain.request.RegisterRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.LoginResponse;
import xcode.ilmugiziku.domain.response.UserResponse;

import java.util.List;

public interface AuthPresenter {
   BaseResponse<LoginResponse> login(LoginRequest request);
   BaseResponse<CreateBaseResponse> register(RegisterRequest request);
   BaseResponse<List<UserResponse>> getUserList(String token);
   BaseResponse<Boolean> logout(String token);
}
