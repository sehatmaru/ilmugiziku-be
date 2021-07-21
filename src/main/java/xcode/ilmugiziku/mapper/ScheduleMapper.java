package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.ScheduleModel;
import xcode.ilmugiziku.domain.request.schedule.CreateScheduleRequest;
import xcode.ilmugiziku.domain.request.schedule.ScheduleDateRequest;
import xcode.ilmugiziku.domain.response.ScheduleResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class ScheduleMapper {

    public ScheduleResponse modelToResponse(ScheduleModel model) {
        if (model != null) {
            ScheduleResponse response = new ScheduleResponse();
            response.setSchedule(model.getSchedule());
            response.setSecureId(model.getSecureId());
            response.setDesc(model.getDescription());
            response.setStartTime(model.getStartTime());
            response.setEndTime(model.getEndTime());

            return response;
        } else {
            return null;
        }
    }

    public List<ScheduleResponse> modelsToResponses(List<ScheduleModel> models) {
        if (models != null) {
            List<ScheduleResponse> response = new ArrayList<>();

            for (ScheduleModel model : models) {
                response.add(modelToResponse(model));
            }

            return response;
        } else {
            return null;
        }
    }

    public ScheduleModel createRequestToModel(CreateScheduleRequest request) {
        if (request != null) {
            ScheduleModel response = new ScheduleModel();
            response.setSecureId(generateSecureId());
            response.setAuthSecureId(request.getAuthSecureId());
            response.setDescription(request.getDesc());
            response.setSchedule(request.getSchedule());
            response.setStartTime(request.getStartTime());
            response.setEndTime(request.getEndTime());
            response.setCreatedAt(new Date());

            return response;
        } else {
            return null;
        }
    }

    public ScheduleModel createRequestToModel(ScheduleDateRequest request, String authSecureId) {
        if (request != null) {
            ScheduleModel response = new ScheduleModel();
            response.setSecureId(generateSecureId());
            response.setSchedule(request.getDate());
            response.setDescription(request.getDesc());
            response.setStartTime(request.getStartTime());
            response.setEndTime(request.getEndTime());
            response.setAuthSecureId(authSecureId);
            response.setCreatedAt(new Date());

            return response;
        } else {
            return null;
        }
    }

    public ScheduleModel updateRequestToModel(ScheduleModel model, ScheduleDateRequest request) {
        if (request != null && model != null) {
            model.setSchedule(request.getDate());
            model.setDescription(request.getDesc());
            model.setStartTime(request.getStartTime());
            model.setEndTime(request.getEndTime());
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }
}
