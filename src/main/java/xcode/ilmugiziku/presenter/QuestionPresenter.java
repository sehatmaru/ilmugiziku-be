package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.QuestionResponse;

import java.util.List;

public interface QuestionPresenter {
   BaseResponse<List<QuestionResponse>> getQuizQuestions();

   BaseResponse<List<QuestionResponse>> getPracticeQuestions();

   BaseResponse<List<QuestionResponse>> getTryOutQuestion(int questionType, int questionSubType);
}
