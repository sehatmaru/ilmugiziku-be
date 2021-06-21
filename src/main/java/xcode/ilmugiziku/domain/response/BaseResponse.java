package xcode.ilmugiziku.domain.response;

import lombok.Getter;
import lombok.Setter;

import static xcode.ilmugiziku.shared.ResponseCode.SUCCESS_CODE;
import static xcode.ilmugiziku.shared.ResponseCode.SUCCESS_MESSAGE;

@Getter
@Setter
public class BaseResponse<T> {
    private int statusCode;
    private String message;
    private T result;

    public BaseResponse() {
        this.statusCode = SUCCESS_CODE;
        this.message = SUCCESS_MESSAGE;
    }
}
