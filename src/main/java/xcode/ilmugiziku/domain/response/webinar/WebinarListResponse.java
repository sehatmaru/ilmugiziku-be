package xcode.ilmugiziku.domain.response.webinar;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class WebinarListResponse {
    private String secureId;
    private String title;
    private Date date;
    private double rating;
    private boolean available;
}
