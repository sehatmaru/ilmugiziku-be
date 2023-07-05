package xcode.ilmugiziku.domain.dto;

import xcode.ilmugiziku.domain.model.TokenModel;

public class CurrentUser {
  private static final ThreadLocal<TokenModel> USER_HOLDER = new ThreadLocal<>();

  public static synchronized void set(TokenModel tokenDto) {
    USER_HOLDER.set(tokenDto);
  }

  public static TokenModel get() {
    return USER_HOLDER.get();
  }

  public static synchronized void remove() {
    USER_HOLDER.remove();
  }
}
