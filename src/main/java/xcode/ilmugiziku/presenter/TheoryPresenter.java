package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.CreateTheoryRequest;
import xcode.ilmugiziku.domain.request.UpdateTheoryRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.TheoryResponse;

import java.util.List;

public interface TheoryPresenter {
   BaseResponse<List<TheoryResponse>> getTheoryList(int theoryType);
   BaseResponse<CreateBaseResponse> createTheory(CreateTheoryRequest request);
   BaseResponse<Boolean> updateTheory(UpdateTheoryRequest request);
   BaseResponse<Boolean> deleteTheory(String secureId);
}
