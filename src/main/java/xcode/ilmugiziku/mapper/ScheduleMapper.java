package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.ScheduleModel;
import xcode.ilmugiziku.domain.request.schedule.CreateScheduleRequest;
import xcode.ilmugiziku.domain.request.schedule.UpdateScheduleRequest;
import xcode.ilmugiziku.domain.response.ScheduleResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.Utils.*;

public class ScheduleMapper {

    public ScheduleResponse modelToResponse(ScheduleModel model) {
        if (model != null) {
            ScheduleResponse response = new ScheduleResponse();
            response.setStartDate(model.getStartDate());
            response.setEndDate(model.getEndDate());
            response.setSecureId(model.getSecureId());
            response.setDesc(model.getDescription());
            response.setTimeLimit(model.getTimeLimit());

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
            response.setDescription(request.getDesc());
            response.setStartDate(setDateTime(request.getStartDate(), 7));
            response.setEndDate(setDateTime(request.getEndDate(), 23));
            response.setTimeLimit(request.getTimeLimit());
            response.setCreatedAt(new Date());

            return response;
        } else {
            return null;
        }
    }

    public ScheduleModel updateRequestToModel(ScheduleModel model, UpdateScheduleRequest request) {
        if (request != null && model != null) {
            model.setDescription(request.getDesc());
            model.setStartDate(setDateTime(request.getStartDate(), 7));
            model.setEndDate(setDateTime(request.getEndDate(), 23));
            model.setTimeLimit(request.getTimeLimit());
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }
}
