package xcode.marsiajar.domain.response.webinar;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class WebinarListResponse {
    private String secureId;
    private String title;
    private Date date;
    private String category;
    private String categorySecureId;
    private BigDecimal price;
    private double rating;
    private boolean available;
}
