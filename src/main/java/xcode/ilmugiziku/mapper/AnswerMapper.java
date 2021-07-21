package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.AnswerModel;
import xcode.ilmugiziku.domain.request.CreateAnswerRequest;
import xcode.ilmugiziku.domain.request.UpdateAnswerRequest;
import xcode.ilmugiziku.domain.response.AnswerResponse;
import xcode.ilmugiziku.domain.response.AnswerValueResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

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
            AnswerModel response = new AnswerModel();
            response.setContent(request.getContent());
            response.setValue(request.isValue());
            response.setUpdatedAt(new Date());

            return response;
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

    public List<AnswerResponse> modelsToAnswerResponses(List<AnswerModel> models) {
        if (models != null) {
            List<AnswerResponse> response = new ArrayList<>();

            for (AnswerModel model : models) {
                response.add(modelToAnswerResponse(model));
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

            for (AnswerModel model : models) {
                response.add(modelToAnswerValueResponse(model));
            }

            return response;
        } else {
            return null;
        }
    }
}
