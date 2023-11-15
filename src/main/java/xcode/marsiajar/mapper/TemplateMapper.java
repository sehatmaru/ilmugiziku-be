package xcode.marsiajar.mapper;

import org.springframework.beans.BeanUtils;
import xcode.marsiajar.domain.model.TemplateModel;
import xcode.marsiajar.domain.request.template.CreateUpdateTemplateRequest;
import xcode.marsiajar.domain.response.TemplateResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static xcode.marsiajar.shared.Utils.generateSecureId;

public class TemplateMapper {

    public TemplateResponse modelToResponse(TemplateModel model) {
        if (model != null) {
            TemplateResponse response = new TemplateResponse();
            BeanUtils.copyProperties(model, response);

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
