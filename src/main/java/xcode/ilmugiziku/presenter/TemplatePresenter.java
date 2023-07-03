package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.template.CreateTemplateRequest;
import xcode.ilmugiziku.domain.request.template.UpdateTemplateRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.TemplateResponse;

import java.util.List;

public interface TemplatePresenter {
   BaseResponse<List<TemplateResponse>> getTemplateList(String token, int questionType, int questionSubType);
   BaseResponse<Boolean> setTemplateActive(String token, String secureId);
   BaseResponse<CreateBaseResponse> createTemplate(String token, CreateTemplateRequest request);
   BaseResponse<Boolean> updateTemplate(String token, UpdateTemplateRequest request);
   BaseResponse<Boolean> deleteTemplate(String token, String secureId);
}
