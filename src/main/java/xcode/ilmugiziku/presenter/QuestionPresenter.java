package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.CreateQuestionRequest;
import xcode.ilmugiziku.domain.request.UpdateQuestionRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.QuestionResponse;

import java.util.List;

public interface QuestionPresenter {
   BaseResponse<List<QuestionResponse>> getQuizQuestions();
   BaseResponse<List<QuestionResponse>> getPracticeQuestions();
   BaseResponse<List<QuestionResponse>> getTryOutQuestion(int questionType, int questionSubType);
   BaseResponse<CreateBaseResponse> createQuestion(CreateQuestionRequest request);
   BaseResponse<Boolean> updateQuestion(UpdateQuestionRequest request);
   BaseResponse<Boolean> deleteQuestion(String secureId);
}
