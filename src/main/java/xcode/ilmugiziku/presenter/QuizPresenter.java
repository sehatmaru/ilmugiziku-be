package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.model.QuestionModel;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.QuestionResponse;

import java.util.List;

public interface QuizPresenter {
   QuestionModel findById(int id);

   BaseResponse<List<QuestionResponse>> getQuizQuestions();
}
