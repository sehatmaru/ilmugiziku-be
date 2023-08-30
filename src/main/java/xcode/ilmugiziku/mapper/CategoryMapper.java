package xcode.ilmugiziku.mapper;

import org.springframework.beans.BeanUtils;
import xcode.ilmugiziku.domain.model.CategoryModel;
import xcode.ilmugiziku.domain.request.category.CreateUpdateCategoryRequest;
import xcode.ilmugiziku.domain.response.category.CategoryResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.Utils.generateSecureId;

public class CategoryMapper {

    public CategoryResponse modelToResponse(CategoryModel model) {
        if (model != null) {
            CategoryResponse response = new CategoryResponse();
            BeanUtils.copyProperties(model, response);

            return response;
        } else {
            return null;
        }
    }

    public List<CategoryResponse> modelsToResponses(List<CategoryModel> models) {
        if (models != null) {
            List<CategoryResponse> response = new ArrayList<>();

            for (CategoryModel model : models) {
                response.add(modelToResponse(model));
            }

            return response;
        } else {
            return Collections.emptyList();
        }
    }

    public CategoryModel createRequestToModel(CreateUpdateCategoryRequest request) {
        if (request != null) {
            CategoryModel response = new CategoryModel();
            response.setSecureId(generateSecureId());
            response.setTitle(request.getTitle());
            response.setCreatedAt(new Date());

            return response;
        } else {
            return null;
        }
    }

    public CategoryModel updateRequestToModel(CategoryModel model, CreateUpdateCategoryRequest request) {
        if (request != null && model != null) {
            model.setTitle(request.getTitle());
            model.setUpdatedAt(new Date());

            return model;
        } else {
            return null;
        }
    }

}
