package xcode.ilmugiziku.exception;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import xcode.ilmugiziku.domain.response.BaseResponse;

import static xcode.ilmugiziku.shared.ResponseCode.*;

@ControllerAdvice
public class BaseExceptions extends ResponseEntityExceptionHandler {

    private final BaseResponse<String> response = new BaseResponse<>();

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        response.setFailed(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        response.setInvalidMethod(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        response.setServerError(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        response.setServerError(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        response.setFailed(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        response.setFailed(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<BaseResponse<String>> exception(AppException ex) {
        switch (ex.getMessage()) {
            case TOKEN_ERROR_MESSAGE: {
                response.setInvalidToken();
                break;
            }
            case AUTH_ERROR_MESSAGE:
            case NOT_AUTHORIZED_MESSAGE:
            case MEMBER_NOT_FOUND_MESSAGE: {
                response.setNotAuthorized(ex.getMessage());
                break;
            }
            case NOT_FOUND_MESSAGE:
            case GROUP_NOT_FOUND_MESSAGE:
            case INVALID_CODE:
            case BILL_NOT_FOUND_MESSAGE:
            case EMAIL_NOT_FOUND: {
                response.setNotFound(ex.getMessage());
                break;
            }
            case EXIST_MESSAGE:
            case EMAIL_EXIST:
            case USERNAME_EXIST:
            case USER_EXIST_ON_GROUP: {
                response.setExistData(ex.getMessage());
                break;
            }
            case PARAMS_ERROR_MESSAGE: {
                throw new AppException(PARAMS_ERROR_MESSAGE);
                break;
            }
            case INVALID_PASSWORD: {
                response.setInvalidPassword();
                break;
            }
            case OTP_ERROR_MESSAGE: {
                response.setInvalidOTP();
                break;
            }
            default: response.setFailed(ex.getMessage());
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}