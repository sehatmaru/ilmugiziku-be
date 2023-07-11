package xcode.ilmugiziku.domain.request.exam;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter
@Setter
public class CreateUpdateExamRequest {

    @NotEmpty()
    private String title;
    private String template;

    private boolean available;

    @NotBlank()
    private int maxParticipant;

    private Date startAt;

    private Date endAt;

    public CreateUpdateExamRequest() {
    }

}
