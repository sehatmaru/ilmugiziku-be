package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AuthTokenModel;
import xcode.ilmugiziku.domain.repository.AuthTokenRepository;
import xcode.ilmugiziku.shared.AuthTokenGenerator;

import java.util.Date;

import static xcode.ilmugiziku.shared.Environment.MAX_TOKEN_DAYS;
import static xcode.ilmugiziku.shared.Utils.getDifferenceDays;

@Service
public class AuthTokenService {

   @Autowired private AuthTokenRepository authTokenRepository;

   public boolean isValidToken(String token) {
      return authTokenRepository.findByToken(token) != null;
   }

   public boolean isStillValidToken(AuthTokenModel model) {
      return getDifferenceDays(model.getCreatedAt(), new Date()) < MAX_TOKEN_DAYS;
   }

   public String generateAuthToken(String secureId) {
      String token = new AuthTokenGenerator().showString();
      AuthTokenModel tokenModel = new AuthTokenModel(token, secureId);
      authTokenRepository.save(tokenModel);

      return token;
   }

   public void destroyAuthToken(AuthTokenModel model) {
      authTokenRepository.delete(model);
   }

   public AuthTokenModel getAuthTokenByToken(String token) {
      return authTokenRepository.findByToken(token);
   }

   public String refreshToken(AuthTokenModel model, String secureId) {
      destroyAuthToken(model);

      return generateAuthToken(secureId);
   }

}
