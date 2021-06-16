package xcode.ilmugiziku.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.model.AuthModel;
import xcode.ilmugiziku.domain.request.RegisterRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.LoginResponse;
import xcode.ilmugiziku.presenter.AuthPresenter;

@RestController
@RequestMapping(value = "auth")
public class AuthApi {

    @Autowired
    AuthPresenter authPresenter;

    @PostMapping("/register")
    ResponseEntity<BaseResponse<CreateBaseResponse>> register (@RequestBody @Validated RegisterRequest body) {
        BaseResponse<CreateBaseResponse> response = authPresenter.register(body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/login")
    ResponseEntity<BaseResponse<LoginResponse>> login(@RequestParam @Validated String email, @RequestParam @Validated String password, @RequestParam @Validated int role, @RequestParam @Validated int type) {
        BaseResponse<LoginResponse> response = authPresenter.login(email, password, role, type);

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
//        if (authPresenter.isBookExist(id)) {
//            AuthModel authModel = authPresenter.findById(id);
//            authModel.setDeletedAt(new Date());
//
//            try {
//                authPresenter.update(id, authModel);
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
//        if (authPresenter.isBookExist(request.getId())) {
//            AuthModel authModel = authPresenter.findById(request.getId());
//            authModel.setTitle(request.getTitle());
//            authModel.setPublication(request.getPublication());
//            authModel.setAuthor(request.getAuthor());
//            authModel.setYear(request.getYear());
//            authModel.setPrice(request.getPrice());
//            authModel.setUpdatedAt(new Date());
//
//            try {
//                authPresenter.update(request.getId(), authModel);
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
//            authModels = authPresenter.findAll();
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
