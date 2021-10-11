package xcode.ilmugiziku.domain.request.lesson;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UpdateLessonRequest {
    private String title;
    private String theory;
    private String videoUri;

    public UpdateLessonRequest() {
    }

    public boolean validate() {
        return !title.isEmpty();
    }
}
