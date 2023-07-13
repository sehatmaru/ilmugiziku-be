package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.ExamModel;
import xcode.ilmugiziku.domain.model.UserExamRelModel;
import xcode.ilmugiziku.domain.request.exam.CreateUpdateExamRequest;
import xcode.ilmugiziku.domain.response.exam.ExamResponse;
import xcode.ilmugiziku.domain.response.exam.RankResponse;

import java.util.ArrayList;
import java.util.Collections;
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
            response.setSecureId(model.getSecureId());
            response.setTitle(model.getTitle());
            response.setAvailableSlot(model.getMaxParticipant() - model.getCurrentParticipant());
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
            return Collections.emptyList();
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

    public RankResponse modelToRankResponse(UserExamRelModel request, String name) {
        if (request != null) {
            RankResponse model = new RankResponse();
            model.setDuration(request.getDuration());
            model.setIncorrect(request.getIncorrect());
            model.setCorrect(request.getCorrect());
            model.setBlank(request.getBlank());
            model.setScore(request.getScore());
            model.setStartTime(request.getStartTime());
            model.setFinishTime(request.getFinishTime());
            model.setName(name);

            return model;
        } else {
            return null;
        }
    }

}
