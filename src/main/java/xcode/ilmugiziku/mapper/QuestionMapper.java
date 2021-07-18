package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.QuestionModel;
import xcode.ilmugiziku.domain.request.CreateQuestionRequest;
import xcode.ilmugiziku.domain.request.UpdateQuestionRequest;
import xcode.ilmugiziku.domain.response.QuestionResponse;

import java.util.Date;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class QuestionMapper {

    public QuestionResponse modelToResponse(QuestionModel model) {
        if (model != null) {
            QuestionResponse response = new QuestionResponse();
            response.setSecureId(model.getSecureId());
            response.setContent(model.getContent());
            response.setQuestionType(model.getQuestionType());
            response.setQuestionSubType(model.getQuestionSubType());

            return response;
        } else {
            return null;
        }
    }

    public QuestionModel createRequestToModel(CreateQuestionRequest request) {
        if (request != null) {
            QuestionModel response = new QuestionModel();
            response.setSecureId(generateSecureId());
            response.setContent(request.getContent());
            response.setQuestionType(request.getQuestionType());
            response.setQuestionSubType(request.getQuestionSubType());
            response.setCreatedAt(new Date());

            return response;
        } else {
            return null;
        }
    }

    public QuestionModel updateRequestToModel(QuestionModel model, UpdateQuestionRequest request) {
        if (request != null && model != null) {
            model.setContent(request.getContent());
            model.setQuestionType(request.getQuestionType());
            model.setQuestionSubType(request.getQuestionSubType());
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }
}
