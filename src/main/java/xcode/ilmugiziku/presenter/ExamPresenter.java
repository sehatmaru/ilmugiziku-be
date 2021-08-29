package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.exam.CreateExamRequest;
import xcode.ilmugiziku.domain.response.*;
import xcode.ilmugiziku.domain.response.exam.*;

import java.util.List;

public interface ExamPresenter {
   BaseResponse<CreateExamResponse> submitExam(String token, CreateExamRequest request);
   BaseResponse<List<ExamResultResponse>> getExamResult(String token, int questionType);
   BaseResponse<List<ExamRankResponse>> getExamRank(String token, int questionType, int questionSubType);
   BaseResponse<List<ExamKeyResponse>> getExamKey(String token, int questionType, int questionSubType);
   BaseResponse<List<ExamInformationResponse>> getExamInformation(String token, int questionType);
   BaseResponse<List<ExamStatusResponse>> getExamStatus(String token, int questionType);
}
