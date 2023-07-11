package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.ExamModel;
import xcode.ilmugiziku.domain.request.exam.CreateUpdateExamRequest;
import xcode.ilmugiziku.domain.response.exam.ExamResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class ExamMapper {
    public ExamModel createRequestToModel(CreateUpdateExamRequest request) {
        if (request != null) {
            ExamModel result = new ExamModel();
            result.setSecureId(generateSecureId());
            result.setTitle(request.getTitle());
            result.setMaxParticipant(request.getMaxParticipant());
            result.setTemplate(request.getTemplate());
            result.setStartAt(request.getStartAt());
            result.setEndAt(request.getEndAt());
            result.setAvailable(true);
            result.setCreatedAt(new Date());

            return result;
        } else {
            return null;
        }
    }

    public ExamResponse modelToResponse(ExamModel model) {
        if (model != null) {
            ExamResponse response = new ExamResponse();
            response.setTitle(model.getTitle());
            response.setMaxParticipant(model.getMaxParticipant());
            response.setCurrentParticipant(model.getCurrentParticipant());
            response.setTemplate(model.getTemplate());
            response.setStartAt(model.getStartAt());
            response.setEndAt(model.getEndAt());
            response.setAvailable(model.isAvailable());
            response.setCreatedAt(model.getCreatedAt());

            return response;
        } else {
            return null;
        }
    }

    public List<ExamResponse> modelsToResponses(List<ExamModel> models) {
        if (models != null) {
            List<ExamResponse> response = new ArrayList<>();

            for (ExamModel model : models) {
                response.add(modelToResponse(model));
            }

            return response;
        } else {
            return null;
        }
    }

    public ExamModel updateRequestToModel(ExamModel model, CreateUpdateExamRequest request) {
        if (request != null && model != null) {
            model.setTitle(request.getTitle());
            model.setMaxParticipant(request.getMaxParticipant());
            model.setTemplate(request.getTemplate());
            model.setStartAt(request.getStartAt());
            model.setEndAt(request.getEndAt());
            model.setAvailable(model.isAvailable());
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

}
