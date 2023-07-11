package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.CourseModel;
import xcode.ilmugiziku.domain.request.course.CreateUpdateCourseRequest;
import xcode.ilmugiziku.domain.response.course.CourseResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class CourseMapper {

    public CourseResponse modelToResponse(CourseModel model) {
        if (model != null) {
            CourseResponse response = new CourseResponse();
            response.setSecureId(model.getSecureId());
            response.setTitle(model.getTitle());
            response.setPrice(model.getPrice());
            response.setAvailable(model.isAvailable());
            response.setCourseType(model.getCourseType());
            response.setAvailable(model.isAvailable());
            response.setRating(model.getRating());

            return response;
        } else {
            return null;
        }
    }

    public List<CourseResponse> modelsToResponses(List<CourseModel> models) {
        if (models != null) {
            List<CourseResponse> response = new ArrayList<>();

            models.forEach(e -> response.add(modelToResponse(e)));

            return response;
        } else {
            return Collections.emptyList();
        }
    }

    public CourseModel createRequestToModel(CreateUpdateCourseRequest request) {
        if (request != null) {
            CourseModel model = new CourseModel();
            model.setSecureId(generateSecureId());
            model.setTitle(request.getTitle());
            model.setPrice(request.getPrice());
            model.setCourseType(request.getCourseType());
            model.setAvailable(true);
            model.setCreatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public CourseModel updateRequestToModel(CourseModel model, CreateUpdateCourseRequest request) {
        if (request != null && model != null) {
            model.setTitle(request.getTitle());
            model.setPrice(request.getPrice());
            model.setCourseType(request.getCourseType());
            model.setAvailable(request.isOpen());
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public List<CourseResponse> userCoursesToResponses(List<CourseModel> models) {
        if (models != null) {
            List<CourseResponse> response = new ArrayList<>();

            for (CourseModel model : models) {
                response.add(userCourseToResponse(model));
            }

            return response;
        } else {
            return Collections.emptyList();
        }
    }

    public CourseResponse userCourseToResponse(CourseModel model) {
        if (model != null) {
            CourseResponse response = new CourseResponse();
            response.setTitle(model.getTitle());
            response.setSecureId(model.getSecureId());
            response.setTitle(model.getTitle());
            response.setPrice(model.getPrice());
            response.setCourseType(model.getCourseType());

            return response;
        } else {
            return null;
        }
    }

}
