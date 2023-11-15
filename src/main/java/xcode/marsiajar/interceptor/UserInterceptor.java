package xcode.marsiajar.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import xcode.marsiajar.domain.dto.CurrentUser;
import xcode.marsiajar.domain.enums.RoleEnum;
import xcode.marsiajar.domain.model.TokenModel;
import xcode.marsiajar.domain.model.UserModel;
import xcode.marsiajar.domain.repository.TokenRepository;
import xcode.marsiajar.domain.repository.UserRepository;
import xcode.marsiajar.exception.AppException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static xcode.marsiajar.shared.ResponseCode.NOT_AUTHORIZED_MESSAGE;
import static xcode.marsiajar.shared.ResponseCode.TOKEN_ERROR_MESSAGE;

@Slf4j
@Component
@EnableAsync
public class UserInterceptor implements HandlerInterceptor {

  @Autowired TokenRepository tokenRepository;
  @Autowired UserRepository userRepository;
  @Autowired Environment environment;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    String token = request.getHeader("Authorization");
    String xenditCallbackToken = request.getHeader("X-CALLBACK-TOKEN");
    String url = request.getRequestURI();

    TokenModel tokenModel = new TokenModel();

    if (token != null) {
      tokenModel = tokenRepository.findByToken(token.substring(7));

      if (tokenModel != null) {
        UserModel userModel = userRepository.getUserBySecureId(tokenModel.getUserSecureId());

        if (url.contains("admin") && userModel.getRole() != RoleEnum.ADMIN) {
          throw new AppException(NOT_AUTHORIZED_MESSAGE);
        }

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
