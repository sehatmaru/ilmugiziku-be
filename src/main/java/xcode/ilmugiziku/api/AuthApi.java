package xcode.ilmugiziku.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.request.auth.LoginRequest;
import xcode.ilmugiziku.domain.request.auth.RegisterRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.auth.LoginResponse;
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
    ResponseEntity<BaseResponse<LoginResponse>> login(@RequestBody @Validated LoginRequest request) {
        BaseResponse<LoginResponse> response = authService.login(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/logout")
    ResponseEntity<BaseResponse<Boolean>> logout(@RequestParam @Validated String token) {
        BaseResponse<Boolean> response = authService.logout(token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
//
//    @DeleteMapping("/delete")
//    ResponseEntity<BaseResponse<Integer>> delete(@RequestParam @Validated int id) {
//        BaseResponse<Integer> response = new BaseResponse<>();
//
//        if (authService.isBookExist(id)) {
//            AuthModel authModel = authService.findById(id);
//            authModel.setDeletedAt(new Date());
//
//            try {
//                authService.update(id, authModel);
//
//                response.setCode(SUCCESS_CODE);
//                response.setMessage(SUCCESS_MESSAGE);
//                response.setData(id);
//            } catch (Exception e) {
//                response.setCode(FAILED_CODE);
//                response.setMessage(e.toString());
//            }
//        } else {
//            response.setCode(NOT_FOUND_CODE);
//            response.setMessage(NOT_FOUND_MESSAGE);
//        }
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(response);
//    }
//
//    @PostMapping("/update")
//    ResponseEntity<BaseResponse<AuthModel>> update (@RequestBody @Validated AuthModel request) {
//        BaseResponse<AuthModel> response = new BaseResponse<>();
//
//        if (authService.isBookExist(request.getId())) {
//            AuthModel authModel = authService.findById(request.getId());
//            authModel.setTitle(request.getTitle());
//            authModel.setPublication(request.getPublication());
//            authModel.setAuthor(request.getAuthor());
//            authModel.setYear(request.getYear());
//            authModel.setPrice(request.getPrice());
//            authModel.setUpdatedAt(new Date());
//
//            try {
//                authService.update(request.getId(), authModel);
//
//                response.setCode(SUCCESS_CODE);
//                response.setMessage(SUCCESS_MESSAGE);
//                response.setData(authModel);
//            } catch (Exception e) {
//                response.setCode(FAILED_CODE);
//                response.setMessage(e.toString());
//            }
//        } else {
//            response.setCode(NOT_FOUND_CODE);
//            response.setMessage(NOT_FOUND_MESSAGE);
//        }
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(response);
//    }
//
//    @GetMapping("/list")
//    ResponseEntity<BaseResponse<List<AuthModel>>> getAll () {
//        List<AuthModel> authModels;
//
//        BaseResponse<List<AuthModel>> response = new BaseResponse<>();
//
//        try {
//            authModels = authService.findAll();
//
//            response.setCode(SUCCESS_CODE);
//            response.setMessage(SUCCESS_MESSAGE);
//            response.setData(authModels);
//        } catch (Exception e) {
//            response.setCode(FAILED_CODE);
//            response.setMessage(e.toString());
//        }
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(response);
//    }
}
