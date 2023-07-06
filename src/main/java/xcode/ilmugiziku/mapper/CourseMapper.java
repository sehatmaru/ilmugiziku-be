package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.CourseModel;
import xcode.ilmugiziku.domain.request.course.CreateUpdateCourseRequest;
import xcode.ilmugiziku.domain.request.course.BenefitRequest;
import xcode.ilmugiziku.domain.response.course.CourseBenefitResponse;
import xcode.ilmugiziku.domain.response.course.CourseResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class CourseMapper {
    public List<CourseBenefitResponse> modelToBenefitResponses(CourseModel model) {
        if (model != null) {
            List<CourseBenefitResponse> response = new ArrayList<>();

            // TODO: 05/07/23
//            String[] features = stringToArray(model.getBenefits());
//            String[] availability = stringToArray(model.getAvailability());
//
//            for (int i=0; i < features.length; i++) {
//                CourseBenefitResponse resp = new CourseBenefitResponse(
//                        features[i],
//                        "",
//                        Boolean.parseBoolean(availability[i])
//                );
//
//                response.add(resp);
//            }

            return response;
        } else {
            return Collections.emptyList();
        }
    }

    public CourseResponse modelToResponse(CourseModel model) {
        if (model != null) {
            CourseResponse response = new CourseResponse();
            response.setSecureId(model.getSecureId());
            response.setTitle(model.getTitle());
            response.setPrice(model.getPrice());
            response.setBenefits(modelToBenefitResponses(model));
            response.setOpen(model.isOpen());
            response.setCourseType(model.getCourseType());

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
            model.setOpen(true);
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
            model.setOpen(request.isOpen());
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

    @Deprecated
    private String arrayToString(BenefitRequest[] requests, boolean isFeature) {
        StringBuilder result = new StringBuilder();

        if (isFeature) {
            for (BenefitRequest feature: requests) {
                result.append(feature.getSecureId());

                if (!feature.equals(requests[requests.length - 1])) {
                    result.append(",");
                }
            }
        } else {
            for (BenefitRequest feature: requests) {
//                result.append(feature.isAvailable());

                if (!feature.equals(requests[requests.length - 1])) {
                    result.append(",");
                }
            }
        }

        return result.toString();
    }

    @Deprecated
    public String[] stringToArray(String requests) {
        return requests.isEmpty() ? new String [0] : requests.split(",");
    }
}
