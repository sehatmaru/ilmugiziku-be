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
    private String category;

    private boolean available;

    @NotNull()
    private int maxParticipant;

    @NotNull()
    private int duration;

    @NotNull()
    private Date startAt;

    @NotNull()
    private Date endAt;

    public CreateUpdateExamRequest() {
    }

}
