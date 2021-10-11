package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.webinar.CreateWebinarRequest;
import xcode.ilmugiziku.domain.request.webinar.UpdateWebinarRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.WebinarResponse;

import java.util.List;

public interface WebinarPresenter {
   BaseResponse<List<WebinarResponse>> getWebinarList(String token, int bimbelType);
   BaseResponse<CreateBaseResponse> createWebinar(String token, CreateWebinarRequest request);
   BaseResponse<Boolean> updateWebinar(String token, String secureId, UpdateWebinarRequest request);
   BaseResponse<Boolean> deleteWebinar(String token, String secureId);
}
