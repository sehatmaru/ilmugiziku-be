package xcode.ilmugiziku.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonResponse {
    private String secureId;
    private String title;
    private String theory;
    private String videoUri;
    private String thumbnailUri;
    private double rating;
    private boolean isRated;
}
