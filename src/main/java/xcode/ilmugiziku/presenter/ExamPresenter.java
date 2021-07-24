package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.exam.CreateExamRequest;
import xcode.ilmugiziku.domain.response.*;
import xcode.ilmugiziku.domain.response.exam.CreateExamResponse;
import xcode.ilmugiziku.domain.response.exam.ExamKeyResponse;
import xcode.ilmugiziku.domain.response.exam.ExamRankResponse;
import xcode.ilmugiziku.domain.response.exam.ExamResultResponse;

import java.util.List;

public interface ExamPresenter {
   BaseResponse<CreateExamResponse> submitExam(String token, CreateExamRequest request);

   BaseResponse<List<ExamResultResponse>> getExamResult(String token, int questionType);

   BaseResponse<List<ExamRankResponse>> getExamRank(String token, int questionType, int questionSubType);

   BaseResponse<List<ExamKeyResponse>> getExamKey(String token, int questionType, int questionSubType);
}
