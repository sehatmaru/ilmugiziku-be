package xcode.ilmugiziku.domain.response.question;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.CourseTypeEnum;

@Getter
@Setter
public class QuestionListResponse {
    private String secureId;
    private String content;
    private CourseTypeEnum category;
    private String createdBy;
    private String editedBy;

}
