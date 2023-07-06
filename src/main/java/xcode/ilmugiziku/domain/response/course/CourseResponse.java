package xcode.ilmugiziku.domain.response.course;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.CourseTypeEnum;

import java.util.List;

@Getter
@Setter
public class CourseResponse {
    private String secureId;
    private String title;
    private int price;
    private CourseTypeEnum courseType;
    private boolean isOpen;
    private List<CourseBenefitResponse> benefits;

    public CourseResponse() {}
}
