package xcode.ilmugiziku.mapper;

import xcode.ilmugiziku.domain.model.PackageModel;
import xcode.ilmugiziku.domain.request.pack.CreatePackageRequest;
import xcode.ilmugiziku.domain.request.pack.PackageFeatureRequest;
import xcode.ilmugiziku.domain.request.pack.UpdatePackageRequest;
import xcode.ilmugiziku.domain.response.pack.PackageFeatureResponse;
import xcode.ilmugiziku.domain.response.pack.PackageResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class PackageMapper {
    public List<PackageFeatureResponse> modelToFeatureResponses(PackageModel model) {
        if (model != null) {
            List<PackageFeatureResponse> response = new ArrayList<>();

            String[] features = stringToArray(model.getFeatures());
            String[] availability = stringToArray(model.getAvailability());

            for (int i=0; i< features.length; i++) {
                PackageFeatureResponse resp = new PackageFeatureResponse(
                        features[i],
                        "",
                        Boolean.parseBoolean(availability[i])
                );

                response.add(resp);
            }

            return response;
        } else {
            return null;
        }
    }

    public PackageResponse modelToResponse(PackageModel model) {
        if (model != null) {
            PackageResponse response = new PackageResponse();
            response.setSecureId(model.getSecureId());
            response.setTitle(model.getTitle());
            response.setPrice(model.getPrice());
            response.setFeatures(modelToFeatureResponses(model));

            return response;
        } else {
            return null;
        }
    }

    public List<PackageResponse> modelsToResponses(List<PackageModel> models) {
        if (models != null) {
            List<PackageResponse> response = new ArrayList<>();

            for (PackageModel model : models) {
                response.add(modelToResponse(model));
            }

            return response;
        } else {
            return null;
        }
    }

    public PackageModel createRequestToModel(CreatePackageRequest request) {
        if (request != null) {
            PackageModel response = new PackageModel();
            response.setSecureId(generateSecureId());
            response.setTitle(request.getTitle());
            response.setPrice(request.getPrice());
            response.setFeatures(arrayToString(request.getFeatures(), true));
            response.setAvailability(arrayToString(request.getFeatures(), false));
            response.setCreatedAt(new Date());

            return response;
        } else {
            return null;
        }
    }

    public PackageModel updateRequestToModel(PackageModel model, UpdatePackageRequest request) {
        if (request != null && model != null) {
            model.setTitle(request.getTitle());
            model.setPrice(request.getPrice());
            model.setFeatures(arrayToString(request.getFeatures(), true));
            model.setAvailability(arrayToString(request.getFeatures(), false));
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

    private String arrayToString(PackageFeatureRequest[] requests, boolean isFeature) {
        StringBuilder result = new StringBuilder();

        if (isFeature) {
            for (PackageFeatureRequest feature: requests) {
                result.append(feature.getSecureId());

                if (!feature.equals(requests[requests.length - 1])) {
                    result.append(",");
                }
            }
        } else {
            for (PackageFeatureRequest feature: requests) {
                result.append(feature.isAvailable());

                if (!feature.equals(requests[requests.length - 1])) {
                    result.append(",");
                }
            }
        }

        return result.toString();
    }

    public String[] stringToArray(String requests) {
        return requests.split(",");
    }
}
