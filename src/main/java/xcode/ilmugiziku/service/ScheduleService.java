package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.ScheduleModel;
import xcode.ilmugiziku.domain.repository.ScheduleRepository;
import xcode.ilmugiziku.domain.request.schedule.CreateScheduleRequest;
import xcode.ilmugiziku.domain.request.schedule.UpdateScheduleRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.ScheduleResponse;
import xcode.ilmugiziku.mapper.ScheduleMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.TOKEN_ERROR_MESSAGE;

@Service
public class ScheduleService {

   @Autowired private AuthTokenService authTokenService;
   @Autowired private ScheduleRepository scheduleRepository;

   private final ScheduleMapper scheduleMapper = new ScheduleMapper();

   public BaseResponse<List<ScheduleResponse>> getScheduleList(String token) {
      BaseResponse<List<ScheduleResponse>> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         List<ScheduleModel> models = new ArrayList<>();

         try {
            models = scheduleRepository.findByDeletedAtIsNull();
         } catch (Exception e) {
            response.setFailed(e.toString());
         }

         response.setSuccess(scheduleMapper.modelsToResponses(models));
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<CreateBaseResponse> createSchedule(String token, CreateScheduleRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      if (authTokenService.isValidToken(token)) {
         if (request.validate()) {
            if (isScheduleExist("", request.getStartDate(), request.getEndDate())){
               response.setExistData("");
            } else {
               ScheduleModel model = scheduleMapper.createRequestToModel(request);

               if (create(model)) {
                  createResponse.setSecureId(model.getSecureId());
                  response.setSuccess(createResponse);
               } else {
                  response.setFailed("");
               }
            }
         } else {
            response.setWrongParams();
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<Boolean> updateSchedule(String token, String scheduleSecureId, UpdateScheduleRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         if (request.validate()) {
            ScheduleModel model = scheduleRepository.findBySecureIdAndDeletedAtIsNull(scheduleSecureId);

            if (isScheduleExist(scheduleSecureId, request.getStartDate(), request.getEndDate())) {
               response.setExistData("");
            } else {
               if (model != null) {
                  try {
                     scheduleRepository.save(scheduleMapper.updateRequestToModel(model, request));

                     response.setSuccess(true);
                  } catch (Exception e){
                     response.setFailed(e.toString());
                  }
               } else {
                  response.setNotFound("");
               }
            }
         } else {
            response.setWrongParams();
         }
      } else {
         response.setFailed(TOKEN_ERROR_MESSAGE);
      }

      return response;
   }

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

   public BaseResponse<Boolean> checkSchedule(String token) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      if (authTokenService.isValidToken(token)) {
         boolean result = false;
         List<ScheduleModel> models = scheduleRepository.findByDeletedAtIsNull();

         for (ScheduleModel model : models) {
            if (model.getStartDate().before(new Date()) && model.getEndDate().after(new Date())) {
               result = true;
               break;
            }
         }

         response.setSuccess(result);
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

   public ScheduleModel getScheduleByDate(Date date) {
      List<ScheduleModel> schedules =  scheduleRepository.findByDeletedAtIsNull();
      ScheduleModel model = new ScheduleModel();

      for (ScheduleModel schedule: schedules) {
         if (date.after(schedule.getStartDate()) && date.before(schedule.getEndDate())) {
            model = schedule;
         }
      }

      return model;
   }

   public boolean isScheduleExist(String secureId, Date startDate, Date endDate) {
      boolean result = false;

      List<ScheduleModel> schedules = scheduleRepository.findByDeletedAtIsNull();

      for (ScheduleModel model : schedules) {
         if (!secureId.isEmpty()) {
            if (!model.getSecureId().equals(secureId)) {
               if (startDate.after(model.getStartDate()) && startDate.before(model.getEndDate())) {
                  result = true;
               }

               if (endDate.after(model.getStartDate()) && endDate.before(model.getEndDate())) {
                  result = true;
               }

               if (startDate == model.getStartDate() || endDate == model.getEndDate()) {
                  result = true;
               }
            }
         } else {
            if (startDate.after(model.getStartDate()) && startDate.before(model.getEndDate())) {
               result = true;
            }

            if (endDate.after(model.getStartDate()) && endDate.before(model.getEndDate())) {
               result = true;
            }

            if (startDate == model.getStartDate() || endDate == model.getEndDate()) {
               result = true;
            }
         }
      }

      return result;
   }
}
