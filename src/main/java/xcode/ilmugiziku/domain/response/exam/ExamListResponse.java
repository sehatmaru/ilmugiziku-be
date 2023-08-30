package xcode.ilmugiziku.domain.response.exam;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ExamListResponse {
    private String secureId;
    private String title;
    private String template;
    private boolean available;
    private int availableSlot;
    private Date createdAt;
}
