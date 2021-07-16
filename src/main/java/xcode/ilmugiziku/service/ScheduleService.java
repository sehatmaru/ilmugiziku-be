package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AuthModel;
import xcode.ilmugiziku.domain.model.ScheduleModel;
import xcode.ilmugiziku.domain.repository.AuthRepository;
import xcode.ilmugiziku.domain.repository.ScheduleRepository;
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

   private final AuthTokenService authTokenService;

   private final ScheduleRepository scheduleRepository;
   private final AuthRepository authRepository;

   public ScheduleService(AuthTokenService authTokenService, ScheduleRepository scheduleRepository, AuthRepository authRepository) {
      this.authTokenService = authTokenService;
      this.scheduleRepository = scheduleRepository;
      this.authRepository = authRepository;
   }

   @Override
   public BaseResponse<List<ScheduleResponse>> getScheduleList(String token, String authSecureId) {
      BaseResponse<List<ScheduleResponse>> response = new BaseResponse<>();
      List<ScheduleResponse> lists = new ArrayList<>();

      if (authTokenService.isValidToken(token)) {
         AuthModel auth = new AuthModel();

         try {
            auth = authRepository.findBySecureIdAndDeletedAtIsNull(authSecureId);
         } catch (Exception e) {
            response.setFailed(e.toString());
         }

         if (auth != null) {
            List<ScheduleModel> models = new ArrayList<>();

            try {
               models = scheduleRepository.findByAuthSecureIdAndDeletedAtIsNullOrderBySchedule(authSecureId);
            } catch (Exception e) {
               response.setFailed(e.toString());
            }

            for (ScheduleModel model : models) {
               ScheduleResponse schedule = new ScheduleResponse();
               schedule.setSchedule(model.getSchedule());
               schedule.setSecureId(model.getSecureId());
               schedule.setDesc(model.getDescription());
               schedule.setStartTime(model.getStartTime());
               schedule.setEndTime(model.getEndTime());

               lists.add(schedule);
            }

            response.setSuccess(lists);
         } else {
            response.setNotFound("");
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   @Override
   public BaseResponse<CreateBaseResponse> createSchedule(String token, CreateScheduleRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      if (authTokenService.isValidToken(token)) {
         if (authRepository.findBySecureIdAndDeletedAtIsNull(request.getAuthSecureId()) != null) {
            ScheduleModel model = new ScheduleModel();
            model.setSecureId(generateSecureId());
            model.setAuthSecureId(request.getAuthSecureId());
            model.setDescription(request.getDesc());
            model.setSchedule(request.getSchedule());
            model.setStartTime(request.getStartTime());
            model.setEndTime(request.getEndTime());
            model.setCreatedAt(new Date());

            if (create(model)) {
               createResponse.setSecureId(model.getSecureId());
               response.setSuccess(createResponse);
            } else {
               response.setFailed("");
            }
         } else {
            response.setNotFound("");
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   @Override
   public BaseResponse<Boolean> updateSchedule(String token, UpdateScheduleRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         if (request.validate()) {
            if (authRepository.findBySecureIdAndDeletedAtIsNull(request.getAuthSecureId()) != null) {
               for (ScheduleDateRequest schedule : request.getSchedules()) {
                  ScheduleModel model = new ScheduleModel();
                  try {
                     model = scheduleRepository.findBySecureIdAndDeletedAtIsNull(schedule.getScheduleSecureId());
                  } catch (Exception e) {
                     response.setFailed(e.toString());
                  }

                  if (model != null) {
                     model.setSchedule(schedule.getDate());
                     model.setDescription(schedule.getDesc());
                     model.setStartTime(schedule.getStartTime());
                     model.setEndTime(schedule.getEndTime());
                     model.setUpdatedAt(new Date());

                     try {
                        scheduleRepository.save(model);

                        response.setSuccess(true);
                     } catch (Exception e){
                        response.setFailed(e.toString());
                     }
                  } else {
                     ScheduleModel sch = new ScheduleModel();
                     sch.setSecureId(generateSecureId());
                     sch.setSchedule(schedule.getDate());
                     sch.setDescription(schedule.getDesc());
                     sch.setStartTime(schedule.getStartTime());
                     sch.setEndTime(schedule.getEndTime());
                     sch.setAuthSecureId(request.getAuthSecureId());
                     sch.setCreatedAt(new Date());

                     if (create(sch)) {
                        response.setSuccess(true);
                     } else {
                        response.setFailed("");
                     }
                  }
               }
            } else {
               response.setNotFound("");
            }
         } else {
            response.setWrongParams();
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   @Override
   public BaseResponse<Boolean> deleteSchedule(String token, String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();
      ScheduleModel model = new ScheduleModel();

      if (authTokenService.isValidToken(token)) {
         try {
            model = scheduleRepository.findBySecureIdAndDeletedAtIsNull(secureId);
         } catch (Exception e) {
            response.setFailed(e.toString());
         }

         if (model != null) {
            model.setDeletedAt(new Date());

            try {
               scheduleRepository.save(model);

               response.setSuccess(true);
            } catch (Exception e){
               response.setFailed(e.toString());
            }
         } else {
            response.setNotFound("");
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
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
