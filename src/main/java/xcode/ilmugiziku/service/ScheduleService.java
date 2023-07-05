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
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.ScheduleMapper;

import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.ResponseCode.*;

@Service
public class ScheduleService {

   @Autowired private ScheduleRepository scheduleRepository;

   private final ScheduleMapper scheduleMapper = new ScheduleMapper();

   public BaseResponse<List<ScheduleResponse>> getScheduleList() {
      BaseResponse<List<ScheduleResponse>> response = new BaseResponse<>();

      List<ScheduleModel> models = scheduleRepository.findByDeletedAtIsNull();

      response.setSuccess(scheduleMapper.modelsToResponses(models));

      return response;
   }

   public BaseResponse<CreateBaseResponse> createSchedule(CreateScheduleRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      if (request.validate()) {
         if (isScheduleExist("", request.getStartDate(), request.getEndDate())){
            throw new AppException(EXIST_MESSAGE);
         } else {
            try {
               ScheduleModel model = scheduleMapper.createRequestToModel(request);

               scheduleRepository.save(model);

               createResponse.setSecureId(model.getSecureId());
               response.setSuccess(createResponse);
            } catch (Exception e){
               throw new AppException(e.toString());
            }
         }
      } else {
         throw new AppException(PARAMS_ERROR_MESSAGE);
      }

      return response;
   }

   public BaseResponse<Boolean> updateSchedule(String scheduleSecureId, UpdateScheduleRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      ScheduleModel model = scheduleRepository.findBySecureIdAndDeletedAtIsNull(scheduleSecureId);

      if (isScheduleExist(scheduleSecureId, request.getStartDate(), request.getEndDate())) {
         throw new AppException(EXIST_MESSAGE);
      } else {
         if (model != null) {
            try {
               scheduleRepository.save(scheduleMapper.updateRequestToModel(model, request));

               response.setSuccess(true);
            } catch (Exception e){
               throw new AppException(e.toString());
            }
         } else {
            throw new AppException(NOT_FOUND_MESSAGE);
         }
      }

      return response;
   }

   public BaseResponse<Boolean> deleteSchedule(String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      ScheduleModel model = scheduleRepository.findBySecureIdAndDeletedAtIsNull(secureId);

      if (model != null) {
         model.setDeletedAt(new Date());

         try {
            scheduleRepository.save(model);

            response.setSuccess(true);
         } catch (Exception e){
            throw new AppException(e.toString());
         }
      } else {
         throw new AppException(NOT_FOUND_MESSAGE);
      }

      return response;
   }

   public BaseResponse<Boolean> checkOnGoingSchedule() {
      BaseResponse<Boolean> response = new BaseResponse<>();

      boolean result = false;
      List<ScheduleModel> models = scheduleRepository.findByDeletedAtIsNull();

      for (ScheduleModel model : models) {
         if (model.getStartDate().before(new Date()) && model.getEndDate().after(new Date())) {
            result = true;
            break;
         }
      }

      response.setSuccess(result);

      return response;
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
               result = checkSchedule(startDate, endDate, result, model);
            }
         } else {
            result = checkSchedule(startDate, endDate, result, model);
         }
      }

      return result;
   }

   private boolean checkSchedule(Date startDate, Date endDate, boolean result, ScheduleModel model) {
      if (startDate.after(model.getStartDate()) && startDate.before(model.getEndDate())) {
         result = true;
      }

      if (endDate.after(model.getStartDate()) && endDate.before(model.getEndDate())) {
         result = true;
      }

      if (startDate == model.getStartDate() || endDate == model.getEndDate()) {
         result = true;
      }
      return result;
   }
}
