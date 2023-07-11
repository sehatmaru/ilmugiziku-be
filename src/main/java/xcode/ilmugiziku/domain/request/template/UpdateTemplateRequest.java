package xcode.ilmugiziku.domain.request.template;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTemplateRequest {
    private String secureId;
    private String name;
    // TODO: 11/07/23  
//    private QuestionTypeEnum questionType;
//    private QuestionSubTypeEnum questionSubType;

    public UpdateTemplateRequest() {
    }

}
