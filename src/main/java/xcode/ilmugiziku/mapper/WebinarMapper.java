package xcode.ilmugiziku.mapper;

import org.springframework.beans.BeanUtils;
import xcode.ilmugiziku.domain.model.WebinarModel;
import xcode.ilmugiziku.domain.request.webinar.CreateUpdateWebinarRequest;
import xcode.ilmugiziku.domain.response.webinar.WebinarListResponse;
import xcode.ilmugiziku.domain.response.webinar.WebinarResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class WebinarMapper {

    public WebinarResponse modelToResponse(WebinarModel model) {
        if (model != null) {
            WebinarResponse response = new WebinarResponse();
            BeanUtils.copyProperties(model, response);
            response.setCategory(model.getCourseType());

            return response;
        } else {
            return null;
        }
    }

    public WebinarListResponse modelToListResponse(WebinarModel model) {
        if (model != null) {
            WebinarListResponse response = new WebinarListResponse();
            BeanUtils.copyProperties(model, response);
            response.setCategory(model.getCourseType());

            return response;
        } else {
            return null;
        }
    }

    public List<WebinarListResponse> modelsToListResponses(List<WebinarModel> models) {
        if (models != null) {
            List<WebinarListResponse> response = new ArrayList<>();

            for (WebinarModel model : models) {
                response.add(modelToListResponse(model));
            }

            return response;
        } else {
            return Collections.emptyList();
        }
    }

    public WebinarModel createRequestToModel(CreateUpdateWebinarRequest request) {
        if (request != null) {
            WebinarModel response = new WebinarModel();
            BeanUtils.copyProperties(request, response);
            response.setSecureId(generateSecureId());
            response.setAvailable(true);
            response.setCreatedAt(new Date());
            response.setUpdatedAt(new Date());

            return response;
        } else {
            return null;
        }
    }

    public WebinarModel updateRequestToModel(WebinarModel model, CreateUpdateWebinarRequest request) {
        if (request != null && model != null) {
            BeanUtils.copyProperties(request, model);
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }
}
