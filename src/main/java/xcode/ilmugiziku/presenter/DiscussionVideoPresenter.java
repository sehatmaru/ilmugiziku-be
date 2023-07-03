package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.discussionvideo.CreateDiscussionVideoRequest;
import xcode.ilmugiziku.domain.request.discussionvideo.UpdateDiscussionVideoRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.DiscussionVideoResponse;

public interface DiscussionVideoPresenter {
   BaseResponse<DiscussionVideoResponse> getDiscussionVideo(String token, String templateSecureId);
   BaseResponse<CreateBaseResponse> createDiscussionVideo(String token, CreateDiscussionVideoRequest request);
   BaseResponse<Boolean> updateDiscussionVideo(String token, UpdateDiscussionVideoRequest request);
   BaseResponse<Boolean> deleteDiscussionVideo(String token, String secureId);
}
