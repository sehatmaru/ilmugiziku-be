package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.QuestionResponse;
import xcode.ilmugiziku.domain.response.ScheduleResponse;

import java.util.List;

public interface SchedulePresenter {
   BaseResponse<List<ScheduleResponse>> getScheduleList(String authSecureId);
}
