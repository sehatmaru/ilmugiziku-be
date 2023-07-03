package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.AnswerModel;
import xcode.ilmugiziku.domain.request.answer.CreateAnswerRequest;
import xcode.ilmugiziku.domain.request.answer.UpdateAnswerRequest;
import xcode.ilmugiziku.domain.response.answer.AnswerResponse;
import xcode.ilmugiziku.domain.response.answer.AnswerValueResponse;

import java.util.*;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;
import static xcode.ilmugiziku.shared.refs.RoleRefs.ADMIN;

public class AnswerMapper {

    public AnswerModel createRequestToModel(CreateAnswerRequest request, String questionSecureId) {
        if (request != null) {
            AnswerModel response = new AnswerModel();
            response.setSecureId(generateSecureId());
            response.setContent(request.getContent());
            response.setQuestionSecureId(questionSecureId);
            response.setValue(request.isValue());
            response.setCreatedAt(new Date());

            return response;
        } else {
            return null;
        }
    }

    public AnswerModel updateRequestToModel(AnswerModel model, UpdateAnswerRequest request) {
        if (model != null && request != null) {
            model.setContent(request.getContent());
            model.setValue(request.isValue());
            model.setUpdatedAt(new Date());

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

    public List<AnswerResponse> modelsToAnswerResponses(List<AnswerModel> models, int role) {
        if (models != null) {
            List<AnswerResponse> response = new ArrayList<>();

            if (role != ADMIN) Collections.shuffle(models, new Random(System.nanoTime()));

            for (int i=models.size()-1; i>=0; i--) {
                response.add(modelToAnswerResponse(models.get(i)));
            }

            return response;
        } else {
            return null;
        }
    }

    public AnswerValueResponse modelToAnswerValueResponse(AnswerModel model) {
        if (model != null) {
            AnswerValueResponse response = new AnswerValueResponse();
            response.setSecureId(model.getSecureId());
            response.setContent(model.getContent());
            response.setValue(model.isValue());

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
            return null;
        }
    }
}
