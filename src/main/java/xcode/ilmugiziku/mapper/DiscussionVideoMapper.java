package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.DiscussionVideoModel;
import xcode.ilmugiziku.domain.request.discussionvideo.CreateDiscussionVideoRequest;
import xcode.ilmugiziku.domain.request.discussionvideo.UpdateDiscussionVideoRequest;

import java.util.Date;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class DiscussionVideoMapper {

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

    public DiscussionVideoModel createRequestToModel(CreateDiscussionVideoRequest request) {
        if (request != null) {
            DiscussionVideoModel model = new DiscussionVideoModel();
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

    public DiscussionVideoModel updateRequestToModel(DiscussionVideoModel model, UpdateDiscussionVideoRequest request) {
        if (request != null && model != null) {
            model.setUri(request.getUri());
            model.setQuestionType(request.getQuestionType());
            model.setQuestionSubType(request.getQuestionSubType());
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }
}
