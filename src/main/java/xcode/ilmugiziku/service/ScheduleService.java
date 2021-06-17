package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AuthModel;
import xcode.ilmugiziku.domain.model.ScheduleModel;
import xcode.ilmugiziku.domain.repository.AuthRepository;
import xcode.ilmugiziku.domain.repository.ScheduleRepository;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.ScheduleResponse;
import xcode.ilmugiziku.presenter.SchedulePresenter;

import java.util.ArrayList;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.*;

@Service
public class ScheduleService implements SchedulePresenter {

   final ScheduleRepository scheduleRepository;
   final AuthRepository authRepository;

   public ScheduleService(ScheduleRepository scheduleRepository, AuthRepository authRepository) {
      this.scheduleRepository = scheduleRepository;
      this.authRepository = authRepository;
   }

   @Override
   public BaseResponse<List<ScheduleResponse>> getScheduleList(String authSecureId) {
      BaseResponse<List<ScheduleResponse>> response = new BaseResponse<>();
      List<ScheduleResponse> lists = new ArrayList<>();

      AuthModel auth = new AuthModel();

      try {
         auth = authRepository.findBySecureIdAndDeletedAtIsNull(authSecureId);
      } catch (Exception e) {
         response.setStatusCode(FAILED_CODE);
         response.setMessage(e.toString());
      }

      if (auth != null) {
         List<ScheduleModel> models = new ArrayList<>();

         try {
            models = scheduleRepository.findByAuthSecureIdAndDeletedAtIsNullOrderBySchedule(authSecureId);
         } catch (Exception e) {
            response.setStatusCode(FAILED_CODE);
            response.setMessage(e.toString());
         }

         for (ScheduleModel model : models) {
            ScheduleResponse schedule = new ScheduleResponse();
            schedule.setSchedule(model.getSchedule());
            schedule.setSecureId(model.getSecureId());

            lists.add(schedule);
         }

         response.setStatusCode(SUCCESS_CODE);
         response.setMessage(SUCCESS_MESSAGE);
         response.setResult(lists);
      } else {
         response.setStatusCode(NOT_FOUND_CODE);
         response.setMessage(NOT_FOUND_MESSAGE);
      }

      return response;
   }
}
