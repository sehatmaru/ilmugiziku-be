package xcode.ilmugiziku.mapper;

import org.springframework.beans.BeanUtils;
import xcode.ilmugiziku.domain.model.ExamModel;
import xcode.ilmugiziku.domain.model.UserExamRelModel;
import xcode.ilmugiziku.domain.request.exam.CreateUpdateExamRequest;
import xcode.ilmugiziku.domain.response.exam.ExamListResponse;
import xcode.ilmugiziku.domain.response.exam.ExamResponse;
import xcode.ilmugiziku.domain.response.exam.RankResponse;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class ExamMapper {
    public ExamModel createRequestToModel(CreateUpdateExamRequest request) {
        if (request != null) {
            ExamModel result = new ExamModel();
            BeanUtils.copyProperties(request, result);
            result.setSecureId(generateSecureId());
            result.setAvailable(true);
            result.setCreatedAt(new Date());

            return result;
        } else {
            return null;
        }
    }

    public ExamListResponse modelToListResponse(ExamModel model) {
        if (model != null) {
            ExamListResponse response = new ExamListResponse();
            BeanUtils.copyProperties(model, response);
            response.setCategorySecureId(model.getCategory());
            response.setAvailableSlot(model.getMaxParticipant() - model.getCurrentParticipant());

            return response;
        } else {
            return null;
        }
    }

    public List<ExamListResponse> modelsToListResponses(List<ExamModel> models) {
        if (models != null) {
            List<ExamListResponse> response = new ArrayList<>();

            for (ExamModel model : models) {
                response.add(modelToListResponse(model));
            }

            return response;
        } else {
            return Collections.emptyList();
        }
    }

    public ExamResponse modelToResponse(ExamModel model) {
        if (model != null) {
            ExamResponse response = new ExamResponse();
            BeanUtils.copyProperties(model, response);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            response.setStartAt(dateFormat.format(model.getStartAt()));
            response.setEndAt(dateFormat.format(model.getEndAt()));
            response.setAvailableSlot(model.getMaxParticipant() - model.getCurrentParticipant());
            response.setTotalSlot(model.getMaxParticipant());

            return response;
        } else {
            return null;
        }
    }

    public ExamModel updateRequestToModel(ExamModel model, CreateUpdateExamRequest request) {
        if (request != null && model != null) {
            BeanUtils.copyProperties(request, model);
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public RankResponse modelToRankResponse(UserExamRelModel request, String name) {
        if (request != null) {
            RankResponse model = new RankResponse();
            BeanUtils.copyProperties(request, model);
            model.setName(name);

            return model;
        } else {
            return null;
        }
    }

}
