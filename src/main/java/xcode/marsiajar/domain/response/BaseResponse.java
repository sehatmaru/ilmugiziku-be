package xcode.marsiajar.domain.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import static xcode.marsiajar.shared.ResponseCode.*;

@Getter
@Setter
public class BaseResponse<T> {
    private int statusCode;
    private String message;
    private T result;

    public BaseResponse() {
    }

    public void setSuccess(T data) {
        this.statusCode = HttpStatus.OK.value();
        this.message = SUCCESS_MESSAGE;
        this.result = data;
    }

    public void setNotFound(String message) {
        this.statusCode = HttpStatus.NOT_FOUND.value();
        this.message = message;
    }

    public void setFailed(String message) {
        this.statusCode = HttpStatus.BAD_REQUEST.value();
        this.message = FAILED_MESSAGE + ": " + message;
    }

    public void setInvalidToken() {
        this.statusCode = HttpStatus.UNAUTHORIZED.value();
        this.message = TOKEN_ERROR_MESSAGE;
    }

    public void setNotAuthorized(String message) {
        this.statusCode = HttpStatus.UNAUTHORIZED.value();
        this.message = message;
    }

    public void setWrongParams(String message) {
        this.statusCode = HttpStatus.BAD_REQUEST.value();
        this.message = message;
    }

    public void setInvalidPassword() {
        this.statusCode = HttpStatus.UNAUTHORIZED.value();
        this.message = INVALID_PASSWORD;
    }

    public void setInvalidOTP() {
        this.statusCode = HttpStatus.UNAUTHORIZED.value();
        this.message = OTP_ERROR_MESSAGE;
    }

    public void setExistData(String message) {
        this.statusCode = HttpStatus.CONFLICT.value();
        this.message = message;
    }

    public void setInvalidMethod(String message) {
        this.statusCode = HttpStatus.METHOD_NOT_ALLOWED.value();
        this.message = message;
    }

    public void setServerError(String message) {
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.message = message;
    }
}
