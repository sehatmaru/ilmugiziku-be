package xcode.marsiajar.mapper;

import org.springframework.beans.BeanUtils;
import xcode.marsiajar.domain.model.WebinarModel;
import xcode.marsiajar.domain.request.webinar.CreateUpdateWebinarRequest;
import xcode.marsiajar.domain.response.webinar.WebinarListResponse;
import xcode.marsiajar.domain.response.webinar.WebinarResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static xcode.marsiajar.shared.Utils.generateSecureId;

public class WebinarMapper {

    public WebinarResponse modelToResponse(WebinarModel model) {
        if (model != null) {
            WebinarResponse response = new WebinarResponse();
            BeanUtils.copyProperties(model, response);
            response.setCategorySecureId(model.getCategory());

            return response;
        } else {
            return null;
        }
    }

    public WebinarListResponse modelToListResponse(WebinarModel model) {
        if (model != null) {
            WebinarListResponse response = new WebinarListResponse();
            BeanUtils.copyProperties(model, response);
            response.setCategorySecureId(model.getCategory());

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
