package xcode.ilmugiziku.domain.response.course;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseInformationResponse {
    private boolean isUkomActive;
    private boolean isSkbActive;

    public CourseInformationResponse(boolean isUkomActive, boolean isSkbActive) {
        this.isUkomActive = isUkomActive;
        this.isSkbActive = isSkbActive;
    }
}
