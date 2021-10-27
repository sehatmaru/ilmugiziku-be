package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.lesson.CreateLessonRequest;
import xcode.ilmugiziku.domain.request.lesson.UpdateLessonRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.LessonResponse;

import java.util.List;

public interface LessonPresenter {
   BaseResponse<List<LessonResponse>> getLessonList(String token, int bimbelType);
   BaseResponse<CreateBaseResponse> createLesson(String token, CreateLessonRequest request);
   BaseResponse<Boolean> updateLesson(String token, String secureId, UpdateLessonRequest request);
   BaseResponse<Boolean> deleteLesson(String token, String secureId);
   BaseResponse<LessonResponse> getLesson(String token, String secureId);
}
