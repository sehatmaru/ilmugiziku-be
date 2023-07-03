package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.request.theory.CreateTheoryRequest;
import xcode.ilmugiziku.domain.request.theory.UpdateTheoryRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.CreateBaseResponse;
import xcode.ilmugiziku.domain.response.TheoryResponse;

import java.util.List;

public interface TheoryPresenter {
   BaseResponse<List<TheoryResponse>> getTheoryList(String token, int theoryType);
   BaseResponse<CreateBaseResponse> createTheory(String token, CreateTheoryRequest request);
   BaseResponse<Boolean> updateTheory(String token, UpdateTheoryRequest request);
   BaseResponse<Boolean> deleteTheory(String token, String secureId);
}
