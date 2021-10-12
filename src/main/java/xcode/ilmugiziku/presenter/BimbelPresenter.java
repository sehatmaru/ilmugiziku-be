package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.BimbelResponse;

import java.util.List;

public interface BimbelPresenter {
   BaseResponse<BimbelResponse> getBimbelPackage(String token, int bimbelType);
}
