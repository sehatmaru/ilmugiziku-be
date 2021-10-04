package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.discussionvideo.CreateDiscussionVideoRequest;
import xcode.ilmugiziku.domain.request.discussionvideo.UpdateDiscussionVideoRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;

public interface DiscussionVideoPresenter {
   BaseResponse<CreateBaseResponse> createDiscussionVideo(String token, CreateDiscussionVideoRequest request);
   BaseResponse<Boolean> updateDiscussionVideo(String token, UpdateDiscussionVideoRequest request);
   BaseResponse<Boolean> deleteDiscussionVideo(String token, String secureId);
}
