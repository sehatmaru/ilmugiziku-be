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
            case INACTIVE_COURSE:
            case EXAM_FULL:
            case LOGIN_EXIST_MESSAGE: {
                response.setNotAuthorized(ex.getMessage());
                break;
            }
            case NOT_FOUND_MESSAGE:
            case INVALID_CODE:
            case INVOICE_NOT_FOUND_MESSAGE:
            case COURSE_NOT_FOUND_MESSAGE:
            case WEBINAR_NOT_FOUND_MESSAGE:
            case USER_NOT_FOUND_MESSAGE:
            case EMAIL_NOT_FOUND: {
                response.setNotFound(ex.getMessage());
                break;
            }
            case EXIST_MESSAGE:
            case EMAIL_EXIST:
            case USERNAME_EXIST:
            case UNPAID_INVOICE_EXIST:
            case INACTIVE_COURSE_EXIST:
            case INACTIVE_WEBINAR_EXIST:
            case ACTIVE_COURSE_EXIST:
            case ACTIVE_WEBINAR_EXIST:
            case USER_COURSE_EXIST:
            case USER_WEBINAR_EXIST:
            case USER_EXAM_EXIST:
            case RATING_EXIST: {
                response.setExistData(ex.getMessage());
                break;
            }
            case ANSWER_LENGTH_ERROR_MESSAGE:
            case MULTIPLE_CORRECT_ANSWER_ERROR_MESSAGE:
            case PARAMS_ERROR_MESSAGE: {
                response.setWrongParams(ex.getMessage());
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