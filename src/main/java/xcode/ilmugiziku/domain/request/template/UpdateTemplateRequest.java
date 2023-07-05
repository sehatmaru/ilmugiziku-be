package xcode.ilmugiziku.domain.request.template;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.QuestionSubTypeEnum;
import xcode.ilmugiziku.domain.enums.QuestionTypeEnum;

@Getter
@Setter
public class UpdateTemplateRequest {
    private String secureId;
    private String name;
    private QuestionTypeEnum questionType;
    private QuestionSubTypeEnum questionSubType;

    public UpdateTemplateRequest() {
    }

}
