package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.DiscussionVideoModel;
import xcode.ilmugiziku.domain.request.discussionvideo.CreateDiscussionVideoRequest;
import xcode.ilmugiziku.domain.request.discussionvideo.UpdateDiscussionVideoRequest;
import xcode.ilmugiziku.domain.response.DiscussionVideoResponse;

import java.util.Date;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class DiscussionVideoMapper {

    public DiscussionVideoResponse modelToResponse(DiscussionVideoModel model) {
        if (model != null) {
            DiscussionVideoResponse response = new DiscussionVideoResponse();
            response.setSecureId(model.getSecureId());
            response.setUri(model.getUri());

            return response;
        } else {
            return null;
        }
    }

    public DiscussionVideoModel createRequestToModel(CreateDiscussionVideoRequest request) {
        if (request != null) {
            DiscussionVideoModel model = new DiscussionVideoModel();
            model.setSecureId(generateSecureId());
            model.setQuestionSubType(request.getQuestionSubType());
            model.setQuestionType(request.getQuestionType());
            model.setUri(request.getUri());
            model.setTemplateSecureId(request.getTemplateSecureId());
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
