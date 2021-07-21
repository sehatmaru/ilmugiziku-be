package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.CreateExamRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateExamResponse;

public interface ExamPresenter {
   BaseResponse<CreateExamResponse> submitExam(String token, CreateExamRequest request);
}
