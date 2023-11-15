package xcode.marsiajar.mapper;

import org.springframework.beans.BeanUtils;
import xcode.marsiajar.domain.model.CourseModel;
import xcode.marsiajar.domain.request.course.CreateUpdateCourseRequest;
import xcode.marsiajar.domain.response.course.CourseListResponse;
import xcode.marsiajar.domain.response.course.CourseResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static xcode.marsiajar.shared.Utils.generateSecureId;

public class CourseMapper {

    public CourseResponse modelToResponse(CourseModel model) {
        if (model != null) {
            CourseResponse response = new CourseResponse();
            BeanUtils.copyProperties(model, response);
            response.setCategorySecureId(model.getCategory());

            return response;
        } else {
            return null;
        }
    }

    public List<CourseListResponse> modelsToListResponses(List<CourseModel> models) {
        if (models != null) {
            List<CourseListResponse> response = new ArrayList<>();

            models.forEach(e -> response.add(modelToListResponse(e)));

            return response;
        } else {
            return Collections.emptyList();
        }
    }

    public CourseListResponse modelToListResponse(CourseModel model) {
        if (model != null) {
            CourseListResponse response = new CourseListResponse();
            BeanUtils.copyProperties(model,response);
            response.setCategorySecureId(model.getCategory());

            return response;
        } else {
            return null;
        }
    }

    public CourseModel createRequestToModel(CreateUpdateCourseRequest request) {
        if (request != null) {
            CourseModel model = new CourseModel();
            BeanUtils.copyProperties(request, model);
            model.setSecureId(generateSecureId());
            model.setAvailable(true);
            model.setCreatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    public CourseModel updateRequestToModel(CourseModel model, CreateUpdateCourseRequest request) {
        if (request != null && model != null) {
            BeanUtils.copyProperties(request, model);
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
            BeanUtils.copyProperties(model, response);

            return response;
        } else {
            return null;
        }
    }

}
