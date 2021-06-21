package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AuthModel;
import xcode.ilmugiziku.domain.model.QuestionModel;
import xcode.ilmugiziku.domain.model.ScheduleModel;
import xcode.ilmugiziku.domain.repository.AuthRepository;
import xcode.ilmugiziku.domain.repository.ScheduleRepository;
import xcode.ilmugiziku.domain.request.CreateAnswerRequest;
import xcode.ilmugiziku.domain.request.CreateScheduleRequest;
import xcode.ilmugiziku.domain.request.ScheduleDateRequest;
import xcode.ilmugiziku.domain.request.UpdateScheduleRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.ScheduleResponse;
import xcode.ilmugiziku.presenter.SchedulePresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.*;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;

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

   @Override
   public BaseResponse<Boolean> createSchedule(CreateScheduleRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (request.validate()) {
         if (authRepository.findBySecureIdAndDeletedAtIsNull(request.getAuthSecureId()) != null) {
            for (Date schedule : request.getDates()) {
               ScheduleModel model = new ScheduleModel();
               model.setSecureId(generateSecureId());
               model.setAuthSecureId(request.getAuthSecureId());
               model.setSchedule(schedule);
               model.setValid(true);
               model.setCreatedAt(new Date());

               if (!create(model)) {
                  response.setStatusCode(FAILED_CODE);
                  response.setMessage(FAILED_MESSAGE);
               }
            }
         } else {
            response.setStatusCode(NOT_FOUND_CODE);
            response.setMessage(NOT_FOUND_MESSAGE);
         }
      } else {
         response.setStatusCode(PARAMS_CODE);
         response.setMessage(PARAMS_ERROR_MESSAGE);
      }

      return response;
   }

   @Override
   public BaseResponse<Boolean> updateSchedule(UpdateScheduleRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (request.validate()) {
         if (authRepository.findBySecureIdAndDeletedAtIsNull(request.getAuthSecureId()) != null) {
            for (ScheduleDateRequest schedule : request.getSchedules()) {
               ScheduleModel model = new ScheduleModel();
               try {
                  model = scheduleRepository.findBySecureIdAndDeletedAtIsNull(schedule.getScheduleSecureId());
               } catch (Exception e) {
                  response.setStatusCode(FAILED_CODE);
                  response.setMessage(FAILED_MESSAGE);
               }

               if (model != null) {
                  model.setSchedule(schedule.getDate());
                  model.setValid(schedule.isValid());
                  model.setUpdatedAt(new Date());

                  try {
                     scheduleRepository.save(model);

                     response.setResult(true);
                  } catch (Exception e){
                     response.setStatusCode(FAILED_CODE);
                     response.setMessage(FAILED_MESSAGE);
                     response.setResult(false);
                  }
               } else {
                  ScheduleModel sch = new ScheduleModel();
                  sch.setSecureId(generateSecureId());
                  sch.setSchedule(schedule.getDate());
                  sch.setAuthSecureId(request.getAuthSecureId());
                  sch.setCreatedAt(new Date());

                  if (!create(sch)) {
                     response.setStatusCode(FAILED_CODE);
                     response.setMessage(FAILED_MESSAGE);
                  }
               }
            }
         } else {
            response.setStatusCode(NOT_FOUND_CODE);
            response.setMessage(NOT_FOUND_MESSAGE);
         }
      } else {
         response.setStatusCode(PARAMS_CODE);
         response.setMessage(PARAMS_ERROR_MESSAGE);
      }

      return response;
   }

   @Override
   public BaseResponse<Boolean> deleteSchedule(String secureId) {
      return null;
   }

   private boolean create(ScheduleModel model) {
      boolean result = true;

      try {
         scheduleRepository.save(model);
      } catch (Exception e){
         result = false;
      }

      return result;
   }
}
