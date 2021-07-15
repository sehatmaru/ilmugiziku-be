package xcode.ilmugiziku.domain.response;

import lombok.Getter;
import lombok.Setter;

import static xcode.ilmugiziku.shared.ResponseCode.*;

@Getter
@Setter
public class BaseResponse<T> {
    private int statusCode;
    private String message;
    private T result;

    public BaseResponse() {
    }

    public void setSuccess(T data) {
        this.statusCode = SUCCESS_CODE;
        this.message = SUCCESS_MESSAGE;
        this.result = data;
    }

    public void setNotFound(String message) {
        this.statusCode = NOT_FOUND_CODE;

        if (!message.isEmpty()) {
            this.message = message;
        } else {
            this.message = NOT_FOUND_MESSAGE;
        }
    }

    public void setFailed(String message) {
        this.statusCode = FAILED_CODE;
        if (!message.isEmpty()) {
            this.message = message;
        } else {
            this.message = FAILED_MESSAGE;
        }
    }

    public void setWrongParams() {
        this.statusCode = PARAMS_CODE;
        this.message = PARAMS_ERROR_MESSAGE;
    }

    public void setExistData(String message) {
        this.statusCode = EXIST_CODE;

        if (!message.isEmpty()) {
            this.message = message;
        } else {
            this.message = EXIST_MESSAGE;
        }
    }
}
