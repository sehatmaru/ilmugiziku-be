package xcode.ilmugiziku.domain.request.exam;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class CreateUpdateExamRequest {

    @NotBlank()
    private String title;
    private String template;

    private boolean available;

    @NotNull()
    private int maxParticipant;

    @NotNull()
    private int time;

    private Date startAt;

    private Date endAt;

    public CreateUpdateExamRequest() {
    }

}
