package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.dto.CurrentUser;
import xcode.ilmugiziku.domain.enums.RoleEnum;
import xcode.ilmugiziku.domain.model.AnswerModel;
import xcode.ilmugiziku.domain.request.answer.UpdateAnswerRequest;
import xcode.ilmugiziku.domain.request.question.CreateUpdateAnswerRequest;
import xcode.ilmugiziku.domain.response.answer.AnswerResponse;
import xcode.ilmugiziku.domain.response.answer.AnswerValueResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static xcode.ilmugiziku.domain.enums.RoleEnum.ADMIN;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class AnswerMapper {

    public List<AnswerModel> createRequestToModels(List<CreateUpdateAnswerRequest> models, String questionSecureId) {
        if (models != null) {
            List<AnswerModel> response = new ArrayList<>();

            models.forEach(e -> response.add(createRequestToModel(e, questionSecureId)));

            return response;
        } else {
            return Collections.emptyList();
        }
    }

    public AnswerModel createRequestToModel(CreateUpdateAnswerRequest request, String questionSecureId) {
        if (request != null) {
            AnswerModel response = new AnswerModel();
            response.setSecureId(generateSecureId());
            response.setContent(request.getContent());
            response.setCorrectAnswer(request.isCorrectAnswer());
            response.setQuestion(questionSecureId);

            return response;
        } else {
            return null;
        }
    }

    public List<AnswerResponse> modelToResponses(List<AnswerModel> models) {
        if (models != null) {
            List<AnswerResponse> response = new ArrayList<>();

            models.forEach(e -> response.add(modelToResponse(e)));

            if (!CurrentUser.get().isAdmin()) Collections.shuffle(response, new Random(System.nanoTime()));

            return response;
        } else {
            return Collections.emptyList();
        }
    }

    public AnswerResponse modelToResponse(AnswerModel model) {
        if (model != null) {
            AnswerResponse response = new AnswerResponse();
            response.setSecureId(model.getSecureId());
            response.setContent(model.getContent());
            if (CurrentUser.get().isAdmin()) response.setCorrectAnswer(model.isCorrectAnswer());

            return response;
        } else {
            return null;
        }
    }

    public AnswerModel updateRequestToModel(AnswerModel model, UpdateAnswerRequest request) {
        if (model != null && request != null) {
            model.setContent(request.getContent());
            // TODO: 11/07/23  
//            model.setValue(request.isValue());
//            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public AnswerResponse modelToAnswerResponse(AnswerModel model) {
        if (model != null) {
            AnswerResponse response = new AnswerResponse();
            response.setSecureId(model.getSecureId());
            response.setContent(model.getContent());

            return response;
        } else {
            return null;
        }
    }

    public List<AnswerResponse> modelsToAnswerResponses(List<AnswerModel> models, RoleEnum role) {
        if (models != null) {
            List<AnswerResponse> response = new ArrayList<>();

            if (role != ADMIN) Collections.shuffle(models, new Random(System.nanoTime()));

            for (int i=models.size()-1; i>=0; i--) {
                response.add(modelToAnswerResponse(models.get(i)));
            }

            return response;
        } else {
            return Collections.emptyList();
        }
    }

    public AnswerValueResponse modelToAnswerValueResponse(AnswerModel model) {
        if (model != null) {
            AnswerValueResponse response = new AnswerValueResponse();
            response.setSecureId(model.getSecureId());
            response.setContent(model.getContent());
            // TODO: 11/07/23  
//            response.setValue(model.isValue());

            return response;
        } else {
            return null;
        }
    }

    public List<AnswerValueResponse> modelsToAnswerValueResponses(List<AnswerModel> models) {
        if (models != null) {
            List<AnswerValueResponse> response = new ArrayList<>();

            Collections.shuffle(models, new Random(System.nanoTime()));

            for (AnswerModel model : models) {
                response.add(modelToAnswerValueResponse(model));
            }

            return response;
        } else {
            return Collections.emptyList();
        }
    }
}
