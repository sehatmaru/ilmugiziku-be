package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.schedule.CreateScheduleRequest;
import xcode.ilmugiziku.domain.request.schedule.UpdateScheduleRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.ScheduleResponse;

import java.util.List;

public interface SchedulePresenter {
   BaseResponse<List<ScheduleResponse>> getScheduleList(String token, String authSecureId);
   BaseResponse<CreateBaseResponse> createSchedule(String token, CreateScheduleRequest request);
   BaseResponse<Boolean> updateSchedule(String token, UpdateScheduleRequest request);
   BaseResponse<Boolean> deleteSchedule(String token, String secureId);
}
