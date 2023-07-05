package xcode.ilmugiziku.domain.request.schedule;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class UpdateScheduleRequest {
    @NotBlank()
    private String desc;
    @NotNull()
    private Date startDate;
    @NotNull()
    private Date endDate;

    public UpdateScheduleRequest() {
    }

}
