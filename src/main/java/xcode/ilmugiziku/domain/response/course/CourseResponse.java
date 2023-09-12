package xcode.ilmugiziku.domain.response.course;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CourseResponse {
    private String secureId;
    private String title;
    private BigDecimal price;
    private String category;
    private String categorySecureId;
    private boolean available;
    private double rating;
    private List<CourseBenefitResponse> benefits;

    public CourseResponse() {}
}
