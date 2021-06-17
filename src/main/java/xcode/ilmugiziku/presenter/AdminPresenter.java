package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.CreateQuestionRequest;
import xcode.ilmugiziku.domain.request.UpdateQuestionRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;

public interface AdminPresenter {

   BaseResponse<CreateBaseResponse> createQuestion(CreateQuestionRequest request);

   BaseResponse<Boolean> updateQuestion(UpdateQuestionRequest request);

   BaseResponse<Boolean> deleteQuestion(String secureId);
}
