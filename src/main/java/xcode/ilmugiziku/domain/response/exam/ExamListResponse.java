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
    private String category;
    private String categorySecureId;
    private boolean available;
    private boolean registered;
    private String userSecureId;
    private Date finishTime;
    private String status;
    private String statusColor;
    private Date startTime;
    private Date endTime;
    private int availableSlot;
    private Date createdAt;
}
