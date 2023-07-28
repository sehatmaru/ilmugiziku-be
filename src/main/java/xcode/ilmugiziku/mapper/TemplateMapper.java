package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.TemplateModel;
import xcode.ilmugiziku.domain.request.template.CreateUpdateTemplateRequest;
import xcode.ilmugiziku.domain.response.TemplateResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class TemplateMapper {

    public TemplateResponse modelToResponse(TemplateModel model) {
        if (model != null) {
            TemplateResponse response = new TemplateResponse();
            response.setSecureId(model.getSecureId());
            response.setName(model.getName());
            response.setCreatedAt(model.getCreatedAt());

            return response;
        } else {
            return null;
        }
    }

    public List<TemplateResponse> modelsToResponses(List<TemplateModel> models) {
        if (models != null) {
            List<TemplateResponse> response = new ArrayList<>();

            for (TemplateModel model : models) {
                response.add(modelToResponse(model));
            }

            return response;
        } else {
            return Collections.emptyList();
        }
    }

    public TemplateModel createRequestToModel(CreateUpdateTemplateRequest request) {
        if (request != null) {
            TemplateModel response = new TemplateModel();
            response.setSecureId(generateSecureId());
            response.setName(request.getName());
            response.setCreatedAt(new Date());

            return response;
        } else {
            return null;
        }
    }

    public TemplateModel updateRequestToModel(TemplateModel model, CreateUpdateTemplateRequest request) {
        if (request != null && model != null) {
            model.setName(request.getName());
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }
}
