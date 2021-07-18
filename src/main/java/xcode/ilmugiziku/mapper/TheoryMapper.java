package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.TheoryModel;
import xcode.ilmugiziku.domain.request.CreateTheoryRequest;
import xcode.ilmugiziku.domain.request.UpdateTheoryRequest;
import xcode.ilmugiziku.domain.response.TheoryResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class TheoryMapper {

    public TheoryResponse modelToResponse(TheoryModel model) {
        if (model != null) {
            TheoryResponse response = new TheoryResponse();
            response.setSecureId(model.getSecureId());
            response.setCompetence(model.getCompetence());
            response.setTheoryType(model.getTheoryType());
            response.setUri(model.getUri());

            return response;
        } else {
            return null;
        }
    }

    public List<TheoryResponse> modelsToResponses(List<TheoryModel> models) {
        if (models != null) {
            List<TheoryResponse> response = new ArrayList<>();

            for (TheoryModel model : models) {
                response.add(modelToResponse(model));
            }

            return response;
        } else {
            return null;
        }
    }

    public TheoryModel createRequestToModel(CreateTheoryRequest request) {
        if (request != null) {
            TheoryModel response = new TheoryModel();
            response.setSecureId(generateSecureId());
            response.setCompetence(request.getCompetence());
            response.setUri(request.getUri());
            response.setTheoryType(request.getTheoryType());
            response.setCreatedAt(new Date());

            return response;
        } else {
            return null;
        }
    }

    public TheoryModel updateRequestToModel(TheoryModel model, UpdateTheoryRequest request) {
        if (request != null && model != null) {
            model.setCompetence(request.getCompetence());
            model.setUri(request.getUri());
            model.setTheoryType(request.getTheoryType());
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }
}
