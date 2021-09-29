package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.video.CreateVideoRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;

public interface VideoPresenter {
//   BaseResponse<List<VideoResponse>> getVideoList(String token, int theoryType);
   BaseResponse<CreateBaseResponse> createDiscussionVideo(String token, CreateVideoRequest request);
//   BaseResponse<Boolean> updateVideo(String token, UpdateVideoRequest request);
//   BaseResponse<Boolean> deleteVideo(String token, String secureId);
}
