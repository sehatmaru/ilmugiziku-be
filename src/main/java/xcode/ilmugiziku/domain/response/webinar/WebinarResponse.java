package xcode.ilmugiziku.domain.response.webinar;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class WebinarResponse {
    private String secureId;
    private String title;
    private String link;
    private Date date;
    private String category;
    private String categorySecureId;
    private String meetingId;
    private String passcode;
    private BigDecimal price;
    private double rating;
    private boolean available;
}
