package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.bimbel.BimbelInformationResponse;
import xcode.ilmugiziku.domain.response.bimbel.BimbelResponse;

public interface BimbelPresenter {
   BaseResponse<BimbelResponse> getBimbel(String token, int bimbelType);
   BaseResponse<BimbelInformationResponse> getBimbelInformation(String token);
}
