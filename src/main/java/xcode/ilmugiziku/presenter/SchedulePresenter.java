package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.CreateScheduleRequest;
import xcode.ilmugiziku.domain.request.UpdateScheduleRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.ScheduleResponse;

import java.util.List;

public interface SchedulePresenter {
   BaseResponse<List<ScheduleResponse>> getScheduleList(String authSecureId);
   BaseResponse<Boolean> createSchedule(CreateScheduleRequest request);
   BaseResponse<Boolean> updateSchedule(UpdateScheduleRequest request);
   BaseResponse<Boolean> deleteSchedule(String secureId);
}
