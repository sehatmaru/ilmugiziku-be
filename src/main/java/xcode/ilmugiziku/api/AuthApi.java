package xcode.ilmugiziku.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.request.user.AdminLoginRequest;
import xcode.ilmugiziku.domain.request.user.LoginRequest;
import xcode.ilmugiziku.domain.request.user.RegisterRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.user.LoginResponse;
import xcode.ilmugiziku.service.AuthService;

@RestController
@RequestMapping(value = "auth")
public class AuthApi {

    @Autowired private AuthService authService;

    @PostMapping("/register")
    ResponseEntity<BaseResponse<CreateBaseResponse>> register (@RequestBody @Validated RegisterRequest body) {
        BaseResponse<CreateBaseResponse> response = authService.register(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/login")
    ResponseEntity<BaseResponse<LoginResponse>> loginAdmin(@RequestBody @Validated AdminLoginRequest request) {
        BaseResponse<LoginResponse> response = authService.loginAdmin(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/login/consumer")
    ResponseEntity<BaseResponse<LoginResponse>> loginConsumer(@RequestBody @Validated LoginRequest request) {
        BaseResponse<LoginResponse> response = authService.loginConsumer(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/logout")
    ResponseEntity<BaseResponse<Boolean>> logout() {
        BaseResponse<Boolean> response = authService.logout();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
