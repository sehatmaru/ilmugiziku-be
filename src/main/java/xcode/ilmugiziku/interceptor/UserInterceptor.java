package xcode.ilmugiziku.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import xcode.ilmugiziku.domain.dto.CurrentUser;
import xcode.ilmugiziku.domain.model.TokenModel;
import xcode.ilmugiziku.domain.repository.TokenRepository;
import xcode.ilmugiziku.exception.AppException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static xcode.ilmugiziku.shared.ResponseCode.NOT_AUTHORIZED_MESSAGE;
import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;

@Slf4j
@Component
@EnableAsync
public class UserInterceptor implements HandlerInterceptor {

  @Autowired TokenRepository tokenRepository;
  @Autowired Environment environment;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    String token = request.getHeader("Authorization");
    String xenditCallbackToken = request.getHeader("X-CALLBACK-TOKEN");

    TokenModel tokenModel = new TokenModel();

    if (token != null) {
      tokenModel = tokenRepository.findByToken(token.substring(7));

      if (tokenModel != null) {
        if (!tokenModel.isValid()) {
          throw new AppException(TOKEN_ERROR_MESSAGE);
        }
      } else {
        throw new AppException(TOKEN_ERROR_MESSAGE);
      }
    }

    if (xenditCallbackToken != null ){
      if (xenditCallbackToken.equals(environment.getProperty("xendit.callback"))) tokenModel.setToken(xenditCallbackToken);
      else throw new AppException(TOKEN_ERROR_MESSAGE);
    }

    if (token == null && xenditCallbackToken == null) throw new AppException(NOT_AUTHORIZED_MESSAGE);
    else CurrentUser.set(tokenModel);

    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    CurrentUser.remove();
  }

}
