package xcode.ilmugiziku.domain.request.webinar;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class CreateUpdateWebinarRequest {
    @NotBlank()
    private String title;
    @NotBlank()
    private String link;
    @NotNull()
    private Date date;
    private String meetingId;
    private String passcode;
    private String category;
    @NotNull()
    private BigDecimal price;
    private boolean isOpen;

    public CreateUpdateWebinarRequest() {
    }
}
