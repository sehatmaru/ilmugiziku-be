package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.BenefitModel;
import xcode.ilmugiziku.domain.request.benefit.CreateUpdateBenefitRequest;
import xcode.ilmugiziku.domain.response.benefit.BenefitResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class BenefitMapper {

    public BenefitResponse modelToResponse(BenefitModel model) {
        if (model != null) {
            BenefitResponse response = new BenefitResponse();
            response.setSecureId(model.getSecureId());
            response.setDesc(model.getDescription());

            return response;
        } else {
            return null;
        }
    }

    public List<BenefitResponse> modelsToResponses(List<BenefitModel> models) {
        if (models != null) {
            List<BenefitResponse> response = new ArrayList<>();

            for (BenefitModel model : models) {
                response.add(modelToResponse(model));
            }

            return response;
        } else {
            return null;
        }
    }

    public BenefitModel createRequestToModel(CreateUpdateBenefitRequest request) {
        if (request != null) {
            BenefitModel response = new BenefitModel();
            response.setSecureId(generateSecureId());
            response.setDescription(request.getDesc());
            response.setCreatedAt(new Date());

            return response;
        } else {
            return null;
        }
    }

    public BenefitModel updateRequestToModel(BenefitModel model, CreateUpdateBenefitRequest request) {
        if (request != null && model != null) {
            model.setDescription(request.getDesc());
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }
}
