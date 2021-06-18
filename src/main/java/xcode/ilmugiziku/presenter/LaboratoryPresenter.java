package xcode.ilmugiziku.presenter;

import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.LaboratoryValueResponse;
import xcode.ilmugiziku.domain.response.QuestionResponse;

import java.util.List;

public interface LaboratoryPresenter {
   BaseResponse<List<LaboratoryValueResponse>> getLaboratoryValueList();
}
