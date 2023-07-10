package xcode.ilmugiziku.domain.request.webinar;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.CourseTypeEnum;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class CreateUpdateWebinarRequest {
    @NotBlank()
    private String title;
    private String link;
    private Date date;
    private String meetingId;
    private String passcode;
    private CourseTypeEnum courseType;
    private BigDecimal price;
    private boolean isOpen;

    public CreateUpdateWebinarRequest() {
    }
}
