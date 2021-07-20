package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.CreateQuestionRequest;
import xcode.ilmugiziku.domain.request.UpdateQuestionRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.QuestionResponse;

import java.util.List;

public interface ExamPresenter {
   BaseResponse<Boolean> createExam();
   BaseResponse<List<QuestionResponse>> getQuizQuestions(String token);
   BaseResponse<List<QuestionResponse>> getPracticeQuestions(String token);
   BaseResponse<List<QuestionResponse>> getTryOutQuestion(String token, int questionType, int questionSubType);
   BaseResponse<CreateBaseResponse> createQuestion(String token, CreateQuestionRequest request);
   BaseResponse<Boolean> updateQuestion(String token, UpdateQuestionRequest request);
   BaseResponse<Boolean> deleteQuestion(String token, String secureId);
}
