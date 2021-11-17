package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.schedule.CreateScheduleRequest;
import xcode.ilmugiziku.domain.request.schedule.UpdateScheduleRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.ScheduleResponse;

import java.util.List;

public interface SchedulePresenter {
   BaseResponse<List<ScheduleResponse>> getScheduleList(String token);
   BaseResponse<CreateBaseResponse> createSchedule(String token, CreateScheduleRequest request);
   BaseResponse<Boolean> updateSchedule(String token, String scheduleSecureId, UpdateScheduleRequest request);
   BaseResponse<Boolean> deleteSchedule(String token, String secureId);
   BaseResponse<Boolean> checkSchedule(String token);
}
