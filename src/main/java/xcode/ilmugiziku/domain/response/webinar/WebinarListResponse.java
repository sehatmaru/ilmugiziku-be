package xcode.ilmugiziku.domain.response.webinar;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.CourseTypeEnum;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class WebinarListResponse {
    private String secureId;
    private String title;
    private Date date;
    private CourseTypeEnum category;
    private BigDecimal price;
    private double rating;
    private boolean available;
}
