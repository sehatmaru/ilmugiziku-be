package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.LaboratoryValueModel;
import xcode.ilmugiziku.domain.request.laboratory.CreateLaboratoryValueRequest;
import xcode.ilmugiziku.domain.request.laboratory.UpdateLaboratoryValueRequest;
import xcode.ilmugiziku.domain.response.LaboratoryValueResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class LaboratoryValueMapper {

    public LaboratoryValueResponse modelToResponse(LaboratoryValueModel model) {
        if (model != null) {
            LaboratoryValueResponse response = new LaboratoryValueResponse();
            response.setSecureId(model.getSecureId());
            response.setContent(model.getContent());
            response.setValue(model.getValue());

            return response;
        } else {
            return null;
        }
    }

    public List<LaboratoryValueResponse> modelsToResponses(List<LaboratoryValueModel> models) {
        if (models != null) {
            List<LaboratoryValueResponse> response = new ArrayList<>();

            for (LaboratoryValueModel model : models) {
                response.add(modelToResponse(model));
            }

            return response;
        } else {
            return null;
        }
    }

    public LaboratoryValueModel createRequestToModel(CreateLaboratoryValueRequest request) {
        if (request != null) {
            LaboratoryValueModel response = new LaboratoryValueModel();
            response.setSecureId(generateSecureId());
            response.setContent(request.getContent());
            response.setValue(request.getValue());
            response.setCreatedAt(new Date());

            return response;
        } else {
            return null;
        }
    }

    public LaboratoryValueModel updateRequestToModel(LaboratoryValueModel model, UpdateLaboratoryValueRequest request) {
        if (request != null && model != null) {
            model.setContent(request.getContent());
            model.setValue(request.getValue());
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }
}
