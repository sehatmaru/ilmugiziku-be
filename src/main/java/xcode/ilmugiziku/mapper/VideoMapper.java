package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.VideoModel;
import xcode.ilmugiziku.domain.request.video.CreateVideoRequest;

import java.util.Date;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class VideoMapper {

//    public QuestionExamResponse modelToQuestionExamResponse(QuestionModel model, int role) {
//        if (model != null) {
//            QuestionExamResponse response = new QuestionExamResponse();
//            response.setSecureId(model.getSecureId());
//            response.setContent(model.getContent());
//
//            if (role == ADMIN) {
//                response.setDiscussion(model.getDiscussion());
//            }
//
//            return response;
//        } else {
//            return null;
//        }
//    }
//
//    public QuestionAnswerResponse modelToQuestionValueResponse(QuestionModel model) {
//        if (model != null) {
//            QuestionAnswerResponse response = new QuestionAnswerResponse();
//            response.setSecureId(model.getSecureId());
//            response.setContent(model.getContent());
//            response.setDiscussion(model.getDiscussion());
//            response.setLabel(model.getLabel());
//            response.setType(model.getType());
//
//            return response;
//        } else {
//            return null;
//        }
//    }

    public VideoModel createRequestToModel(CreateVideoRequest request) {
        if (request != null) {
            VideoModel model = new VideoModel();
            model.setSecureId(generateSecureId());
            model.setQuestionSubType(request.getQuestionSubType());
            model.setQuestionType(request.getQuestionType());
            model.setUri(request.getUri());
            model.setCreatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

//    public QuestionModel updateRequestToModel(QuestionModel model, UpdateQuestionRequest request) {
//        if (request != null && model != null) {
//            model.setContent(request.getContent());
//            model.setQuestionType(request.getQuestionType());
//            model.setQuestionSubType(request.getQuestionSubType());
//            model.setDiscussion(request.getDiscussion());
//            model.setLabel(request.getLabel());
//            model.setType(request.getType());
//            model.setUpdatedAt(new Date());
//
//            return model;
//        } else {
//            return null;
//        }
//    }
}
