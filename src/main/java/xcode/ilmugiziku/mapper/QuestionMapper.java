package xcode.ilmugiziku.mapper;

import org.springframework.beans.BeanUtils;
import xcode.ilmugiziku.domain.dto.CurrentUser;
import xcode.ilmugiziku.domain.model.QuestionModel;
import xcode.ilmugiziku.domain.request.question.CreateUpdateQuestionRequest;
import xcode.ilmugiziku.domain.response.question.QuestionListResponse;
import xcode.ilmugiziku.domain.response.question.QuestionResponse;

import java.util.*;

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

            if (!CurrentUser.get().isAdmin()) Collections.shuffle(response, new Random(System.nanoTime()));

            return response;
        } else {
            return Collections.emptyList();
        }
    }

    public QuestionResponse modelToResponse(QuestionModel model) {
        if (model != null) {
            QuestionResponse response = new QuestionResponse();
            BeanUtils.copyProperties(model, response);

            return response;
        } else {
            return null;
        }
    }

    public List<QuestionListResponse> modelToListResponses(List<QuestionModel> models) {
        if (models != null) {
            List<QuestionListResponse> response = new ArrayList<>();

            models.forEach(e -> response.add(modelToListResponse(e)));

            if (!CurrentUser.get().isAdmin()) Collections.shuffle(response, new Random(System.nanoTime()));

            return response;
        } else {
            return Collections.emptyList();
        }
    }

    public QuestionListResponse modelToListResponse(QuestionModel model) {
        if (model != null) {
            QuestionListResponse response = new QuestionListResponse();
            BeanUtils.copyProperties(model, response);

            return response;
        } else {
            return null;
        }
    }
}
