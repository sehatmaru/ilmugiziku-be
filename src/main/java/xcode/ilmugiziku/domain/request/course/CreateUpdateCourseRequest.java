package xcode.ilmugiziku.domain.request.course;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.CourseTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CreateUpdateCourseRequest {
    @NotBlank()
    private String title;
    @NotNull()
    private BigDecimal price;
    private boolean open;
    private CourseTypeEnum courseType;
    private List<BenefitRequest> benefits;

    public CreateUpdateCourseRequest() {
    }
}
