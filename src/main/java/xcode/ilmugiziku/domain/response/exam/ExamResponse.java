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
    private int maxParticipant;
    private int currentParticipant;
    private Date startAt;
    private Date endAt;
    private Date createdAt;
    private Date updatedAt;
}
