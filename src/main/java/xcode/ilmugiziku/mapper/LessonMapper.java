package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.LessonModel;
import xcode.ilmugiziku.domain.request.lesson.CreateLessonRequest;
import xcode.ilmugiziku.domain.request.lesson.UpdateLessonRequest;
import xcode.ilmugiziku.domain.response.LessonResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;
import static xcode.ilmugiziku.shared.Utils.round;

public class LessonMapper {

    public LessonResponse modelToResponse(LessonModel model) {
        if (model != null) {
            LessonResponse response = new LessonResponse();
            response.setSecureId(model.getSecureId());
            response.setTitle(model.getTitle());
            response.setRating(round(model.getRating(), 2));
            response.setTheory(model.getTheory());
            response.setVideoUri(model.getVideoUri());
            response.setThumbnailUri(model.getThumbnailUri());

            return response;
        } else {
            return null;
        }
    }

    public List<LessonResponse> modelsToResponses(List<LessonModel> models) {
        if (models != null) {
            List<LessonResponse> response = new ArrayList<>();

            for (LessonModel model : models) {
                response.add(modelToResponse(model));
            }

            return response;
        } else {
            return Collections.emptyList();
        }
    }

    public LessonModel createRequestToModel(CreateLessonRequest request) {
        if (request != null) {
            LessonModel response = new LessonModel();
            response.setSecureId(generateSecureId());
            response.setTitle(request.getTitle());
            response.setCourseType(request.getCourseType());
            response.setTheory(request.getTheory());
            response.setVideoUri(request.getVideoUri());
            response.setThumbnailUri(request.getThumbnailUri());
            response.setRating(0);
            response.setCreatedAt(new Date());

            return response;
        } else {
            return null;
        }
    }

    public LessonModel updateRequestToModel(LessonModel model, UpdateLessonRequest request) {
        if (request != null && model != null) {
            model.setTitle(request.getTitle());
            model.setTheory(request.getTheory());
            model.setVideoUri(request.getVideoUri());
            model.setThumbnailUri(request.getThumbnailUri());
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }
}
