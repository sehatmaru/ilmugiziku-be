package xcode.ilmugiziku.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class WebinarResponse {
    private String secureId;
    private String title;
    private String link;
    private Date date;
    private String meetingId;
    private String passcode;
    private double rating;
    private boolean available;
}
