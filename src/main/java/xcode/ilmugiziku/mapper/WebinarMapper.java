package xcode.ilmugiziku.mapper;

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
            response.setSecureId(model.getSecureId());
            response.setTitle(model.getTitle());
            response.setLink(model.getLink());
            response.setDate(model.getDate());
            response.setMeetingId(model.getMeetingId());
            response.setPasscode(model.getPasscode());
            response.setAvailable(model.isAvailable());
            response.setRating(model.getRating());

            return response;
        } else {
            return null;
        }
    }

    public List<WebinarResponse> modelsToResponses(List<WebinarModel> models) {
        if (models != null) {
            List<WebinarResponse> response = new ArrayList<>();

            for (WebinarModel model : models) {
                response.add(modelToResponse(model));
            }

            return response;
        } else {
            return Collections.emptyList();
        }
    }

    public WebinarListResponse modelToListResponse(WebinarModel model) {
        if (model != null) {
            WebinarListResponse response = new WebinarListResponse();
            response.setSecureId(model.getSecureId());
            response.setTitle(model.getTitle());
            response.setDate(model.getDate());
            response.setAvailable(model.isAvailable());
            response.setRating(model.getRating());

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
            response.setSecureId(generateSecureId());
            response.setTitle(request.getTitle());
            response.setCourseType(request.getCourseType());
            response.setDate(request.getDate());
            response.setMeetingId(request.getMeetingId());
            response.setLink(request.getLink());
            response.setPasscode(request.getPasscode());
            response.setPrice(request.getPrice());
            response.setAvailable(true);
            response.setCreatedAt(new Date());

            return response;
        } else {
            return null;
        }
    }

    public WebinarModel updateRequestToModel(WebinarModel model, CreateUpdateWebinarRequest request) {
        if (request != null && model != null) {
            model.setTitle(request.getTitle());
            model.setDate(request.getDate());
            model.setMeetingId(request.getMeetingId());
            model.setLink(request.getLink());
            model.setPasscode(request.getPasscode());
            model.setCourseType(request.getCourseType());
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }
}
