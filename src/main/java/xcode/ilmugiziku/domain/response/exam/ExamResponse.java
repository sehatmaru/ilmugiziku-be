package xcode.ilmugiziku.domain.response.exam;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ExamResponse {
    private String secureId;
    private String title;
    private String template;
    private boolean available;
    private int duration;
    private int availableSlot;
    private int totalSlot;
    private String startAt;
    private String endAt;
    private String startAtFormatted;
    private String endAtFormatted;
    private Date createdAt;
}
