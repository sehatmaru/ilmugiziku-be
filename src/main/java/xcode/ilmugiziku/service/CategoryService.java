package xcode.ilmugiziku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.ilmugiziku.domain.model.CategoryModel;
import xcode.ilmugiziku.domain.repository.CategoryRepository;
import xcode.ilmugiziku.domain.request.category.CreateUpdateCategoryRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.category.CategoryResponse;
import xcode.ilmugiziku.exception.AppException;
import xcode.ilmugiziku.mapper.CategoryMapper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static xcode.ilmugiziku.shared.ResponseCode.NOT_FOUND_MESSAGE;

@Service
public class CategoryService {

   @Autowired private CategoryRepository categoryRepository;

   private final CategoryMapper packageFeatureMapper = new CategoryMapper();

   public BaseResponse<List<CategoryResponse>> getCategoryList(String name) {
      BaseResponse<List<CategoryResponse>> response = new BaseResponse<>();

      List<CategoryModel> models = categoryRepository.findByDeletedAtIsNullOrderByCreatedAtDesc();
      models = models.stream()
              .filter(e -> e.getTitle().toLowerCase().contains(name.toLowerCase()))
              .collect(Collectors.toList());

      response.setSuccess(packageFeatureMapper.modelsToResponses(models));

      return response;
   }

   public BaseResponse<CreateBaseResponse> createCategory(CreateUpdateCategoryRequest request) {
      BaseResponse<CreateBaseResponse> response = new BaseResponse<>();
      CreateBaseResponse createResponse = new CreateBaseResponse();

      try {
         CategoryModel model = packageFeatureMapper.createRequestToModel(request);
         categoryRepository.save(model);

         createResponse.setSecureId(model.getSecureId());

         response.setSuccess(createResponse);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> updateCategory(String secureId, CreateUpdateCategoryRequest request) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      CategoryModel model = categoryRepository.findBySecureIdAndDeletedAtIsNull(secureId);

      try {
         categoryRepository.save(packageFeatureMapper.updateRequestToModel(model, request));

         response.setSuccess(true);
      } catch (Exception e){
         throw new AppException(e.toString());
      }

      return response;
   }

   public BaseResponse<Boolean> deleteCategory(String secureId) {
      BaseResponse<Boolean> response = new BaseResponse<>();

      CategoryModel model = categoryRepository.findBySecureIdAndDeletedAtIsNull(secureId);

      if (model != null) {
         model.setDeletedAt(new Date());

         try {
            categoryRepository.save(model);

            response.setSuccess(true);
         } catch (Exception e){
            throw new AppException(e.toString());
         }
      } else {
         throw new AppException(NOT_FOUND_MESSAGE);
      }

      return response;
   }
}
