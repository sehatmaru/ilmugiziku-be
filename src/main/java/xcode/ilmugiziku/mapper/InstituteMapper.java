package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.InstituteModel;
import xcode.ilmugiziku.domain.request.institution.CreateInstituteRequest;
import xcode.ilmugiziku.domain.request.institution.UpdateInstituteRequest;
import xcode.ilmugiziku.domain.response.InstituteResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class InstituteMapper {

    public InstituteResponse modelToResponse(InstituteModel model) {
        if (model != null) {
            InstituteResponse response = new InstituteResponse();
            response.setSecureId(model.getSecureId());
            response.setDescription(model.getDescription());
            response.setUri(model.getUri());

            return response;
        } else {
            return null;
        }
    }

    public List<InstituteResponse> modelsToResponses(List<InstituteModel> models) {
        if (models != null) {
            List<InstituteResponse> response = new ArrayList<>();

            for (InstituteModel model : models) {
                response.add(modelToResponse(model));
            }

            return response;
        } else {
            return null;
        }
    }

    public InstituteModel createRequestToModel(CreateInstituteRequest request) {
        if (request != null) {
            InstituteModel response = new InstituteModel();
            response.setSecureId(generateSecureId());
            response.setDescription(request.getDescription());
            response.setUri(request.getUri());
            response.setCreatedAt(new Date());

            return response;
        } else {
            return null;
        }
    }

    public InstituteModel updateRequestToModel(InstituteModel model, UpdateInstituteRequest request) {
        if (request != null && model != null) {
            model.setDescription(request.getDescription());
            model.setUri(request.getUri());
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }
}
