package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.QuestionModel;
import xcode.ilmugiziku.domain.request.question.CreateQuestionRequest;
import xcode.ilmugiziku.domain.request.question.UpdateQuestionRequest;
import xcode.ilmugiziku.domain.response.question.QuestionExamResponse;
import xcode.ilmugiziku.domain.response.question.QuestionAnswerResponse;

import java.util.Date;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class QuestionMapper {

    public QuestionExamResponse modelToQuestionExamResponse(QuestionModel model) {
        if (model != null) {
            QuestionExamResponse response = new QuestionExamResponse();
            response.setSecureId(model.getSecureId());
            response.setContent(model.getContent());

            return response;
        } else {
            return null;
        }
    }

    public QuestionAnswerResponse modelToQuestionValueResponse(QuestionModel model) {
        if (model != null) {
            QuestionAnswerResponse response = new QuestionAnswerResponse();
            response.setSecureId(model.getSecureId());
            response.setContent(model.getContent());
            response.setDiscussion(model.getDiscussion());

            return response;
        } else {
            return null;
        }
    }

    public QuestionModel createRequestToModel(CreateQuestionRequest request) {
        if (request != null) {
            QuestionModel model = new QuestionModel();
            model.setSecureId(generateSecureId());
            model.setContent(request.getContent());
            model.setQuestionType(request.getQuestionType());
            model.setQuestionSubType(request.getQuestionSubType());
            model.setDiscussion(request.getDiscussion());
            model.setCreatedAt(new Date());

            return model;
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
