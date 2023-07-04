package xcode.ilmugiziku.domain.request.lesson;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateLessonRequest {
    @NotBlank()
    private String title;
    @NotBlank()
    private String theory;
    @NotBlank()
    private String videoUri;
    @NotBlank()
    private String thumbnailUri;
    @NotNull()
    private int bimbelType;

    public CreateLessonRequest() {
    }

}
