package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.dto.CurrentUser;
import xcode.ilmugiziku.domain.model.QuestionModel;
import xcode.ilmugiziku.domain.request.question.CreateUpdateQuestionRequest;
import xcode.ilmugiziku.domain.response.question.QuestionResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class QuestionMapper {

    public QuestionModel createRequestToModel(CreateUpdateQuestionRequest request) {
        if (request != null) {
            QuestionModel model = new QuestionModel();
            model.setSecureId(generateSecureId());
            model.setContent(request.getContent());
            model.setCreatedBy(CurrentUser.get().getUserSecureId());
            model.setCreatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public QuestionModel updateRequestToModel(QuestionModel model, CreateUpdateQuestionRequest request) {
        if (request != null && model != null) {
            model.setContent(request.getContent());
            model.setEditedBy(CurrentUser.get().getUserSecureId());
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public List<QuestionResponse> modelToResponses(List<QuestionModel> models) {
        if (models != null) {
            List<QuestionResponse> response = new ArrayList<>();

            models.forEach(e -> response.add(modelToResponse(e)));

            return response;
        } else {
            return Collections.emptyList();
        }
    }

    public QuestionResponse modelToResponse(QuestionModel model) {
        if (model != null) {
            QuestionResponse response = new QuestionResponse();
            response.setSecureId(model.getSecureId());
            response.setContent(model.getContent());
            response.setCreatedBy(model.getCreatedBy());
            response.setEditedBy(model.getEditedBy());

            return response;
        } else {
            return null;
        }
    }
}
