package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.enums.RoleEnum;
import xcode.ilmugiziku.domain.model.QuestionModel;
import xcode.ilmugiziku.domain.request.question.CreateQuestionRequest;
import xcode.ilmugiziku.domain.request.question.UpdateQuestionRequest;
import xcode.ilmugiziku.domain.response.question.QuestionExamResponse;
import xcode.ilmugiziku.domain.response.question.QuestionAnswerResponse;

import java.util.Date;

import static xcode.ilmugiziku.domain.enums.RoleEnum.ADMIN;
import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class QuestionMapper {

    public QuestionExamResponse modelToQuestionExamResponse(QuestionModel model, RoleEnum role) {
        if (model != null) {
            QuestionExamResponse response = new QuestionExamResponse();
            response.setSecureId(model.getSecureId());
            response.setContent(model.getContent());

            if (role == ADMIN) {
                response.setDiscussion(model.getDiscussion());
            }

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
            response.setLabel(model.getLabel());
            response.setType(model.getType());

            return response;
        } else {
            return null;
        }
    }

    public QuestionModel createRequestToModel(CreateQuestionRequest request) {
        if (request != null) {
            QuestionModel model = new QuestionModel();
            model.setSecureId(generateSecureId());
            model.setTemplateSecureId(request.getTemplateSecureId());
            model.setContent(request.getContent());
            // TODO: 11/07/23  
//            model.setQuestionType(request.getQuestionType());
//            model.setQuestionSubType(request.getQuestionSubType());
            model.setDiscussion(request.getDiscussion());
            model.setLabel(request.getLabel());
            model.setType(request.getType());
            model.setCreatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public QuestionModel updateRequestToModel(QuestionModel model, UpdateQuestionRequest request) {
        if (request != null && model != null) {
            model.setContent(request.getContent());
            // TODO: 11/07/23  
//            model.setQuestionType(request.getQuestionType());
//            model.setQuestionSubType(request.getQuestionSubType());
            model.setDiscussion(request.getDiscussion());
            model.setLabel(request.getLabel());
            model.setType(request.getType());
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }
}
