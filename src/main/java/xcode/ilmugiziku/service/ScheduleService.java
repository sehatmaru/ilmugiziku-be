package xcode.ilmugiziku.service;

import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.AuthModel;
import xcode.ilmugiziku.domain.model.ScheduleModel;
import xcode.ilmugiziku.domain.repository.ScheduleRepository;
import xcode.ilmugiziku.domain.request.schedule.CreateScheduleRequest;
import xcode.ilmugiziku.domain.request.schedule.ScheduleDateRequest;
import xcode.ilmugiziku.domain.request.schedule.UpdateScheduleRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.ScheduleResponse;
import xcode.ilmugiziku.mapper.ScheduleMapper;
import xcode.ilmugiziku.presenter.SchedulePresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;

@Service
public class ScheduleService implements SchedulePresenter {

   private final AuthTokenService authTokenService;
   private final AuthService authService;

   private final ScheduleRepository scheduleRepository;

   private final ScheduleMapper scheduleMapper = new ScheduleMapper();

   public ScheduleService(AuthTokenService authTokenService, AuthService authService, ScheduleRepository scheduleRepository) {
      this.authTokenService = authTokenService;
      this.authService = authService;
      this.scheduleRepository = scheduleRepository;
   }

   @Override
   public BaseResponse<List<ScheduleResponse>> getScheduleList(String token, String authSecureId) {
      BaseResponse<List<ScheduleResponse>> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         AuthModel auth = new AuthModel();

         try {
            auth = authService.getActiveAuthBySecureId(authSecureId);
         } catch (Exception e) {
            response.setFailed(e.toString());
         }

         if (auth != null) {
            List<ScheduleModel> models = new ArrayList<>();

            try {
               models = scheduleRepository.findByAuthSecureIdAndDeletedAtIsNull(authSecureId);
            } catch (Exception e) {
               response.setFailed(e.toString());
            }

            response.setSuccess(scheduleMapper.modelsToResponses(models));
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
         if (authService.getActiveAuthBySecureId(request.getAuthSecureId()) != null) {
            ScheduleModel model = scheduleMapper.createRequestToModel(request);

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
            if (authService.getActiveAuthBySecureId(request.getAuthSecureId()) != null) {
               for (ScheduleDateRequest schedule : request.getSchedules()) {
                  ScheduleModel model = new ScheduleModel();
                  try {
                     model = scheduleRepository.findBySecureIdAndDeletedAtIsNull(schedule.getScheduleSecureId());
                  } catch (Exception e) {
                     response.setFailed(e.toString());
                  }

                  if (model != null) {
                     try {
                        scheduleRepository.save(scheduleMapper.updateRequestToModel(model, schedule));

                        response.setSuccess(true);
                     } catch (Exception e){
                        response.setFailed(e.toString());
                     }
                  } else {
                     if (create(scheduleMapper.createRequestToModel(schedule, request.getAuthSecureId()))) {
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

   public ScheduleModel getScheduleByDateAndAuthSecureId(Date date, String authSecureId) {
      List<ScheduleModel> schedules =  scheduleRepository.findByAuthSecureIdAndDeletedAtIsNull(authSecureId);
      ScheduleModel model = new ScheduleModel();

      for (ScheduleModel schedule: schedules) {
         if (date.after(schedule.getStartDate()) && date.before(schedule.getEndDate())) {
            model = schedule;
         }
      }

      return model;
   }
}
