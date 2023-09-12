package xcode.ilmugiziku.domain.response.course;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class CourseListResponse {
    private String secureId;
    private String title;
    private BigDecimal price;
    private String category;
    private String categorySecureId;
    private boolean available;
    private double rating;
    private Date createdAt;

    public CourseListResponse() {}
}
