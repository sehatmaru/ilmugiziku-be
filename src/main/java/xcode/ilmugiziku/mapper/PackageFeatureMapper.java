package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.PackageFeatureModel;
import xcode.ilmugiziku.domain.request.packagefeature.CreatePackageFeatureRequest;
import xcode.ilmugiziku.domain.request.packagefeature.UpdatePackageFeatureRequest;
import xcode.ilmugiziku.domain.response.PackageFeatureResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class PackageFeatureMapper {

    public PackageFeatureResponse modelToResponse(PackageFeatureModel model) {
        if (model != null) {
            PackageFeatureResponse response = new PackageFeatureResponse();
            response.setSecureId(model.getSecureId());
            response.setDesc(model.getDescription());

            return response;
        } else {
            return null;
        }
    }

    public List<PackageFeatureResponse> modelsToResponses(List<PackageFeatureModel> models) {
        if (models != null) {
            List<PackageFeatureResponse> response = new ArrayList<>();

            for (PackageFeatureModel model : models) {
                response.add(modelToResponse(model));
            }

            return response;
        } else {
            return null;
        }
    }

    public PackageFeatureModel createRequestToModel(CreatePackageFeatureRequest request) {
        if (request != null) {
            PackageFeatureModel response = new PackageFeatureModel();
            response.setSecureId(generateSecureId());
            response.setDescription(request.getDesc());
            response.setCreatedAt(new Date());

            return response;
        } else {
            return null;
        }
    }

    public PackageFeatureModel updateRequestToModel(PackageFeatureModel model, UpdatePackageFeatureRequest request) {
        if (request != null && model != null) {
            model.setDescription(request.getDesc());
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }
}
