package xcode.ilmugiziku.domain.request.lesson;

import lombok.Getter;
import lombok.Setter;

import static xcode.ilmugiziku.shared.refs.BimbelTypeRefs.SKB_GIZI;
import static xcode.ilmugiziku.shared.refs.BimbelTypeRefs.UKOM;


@Getter
@Setter
public class CreateLessonRequest {
    private String title;
    private String theory;
    private String videoUri;
    private String thumbnailUri;
    private int bimbelType;

    public CreateLessonRequest() {
    }

    public boolean validate() {
        return !title.isEmpty() && (bimbelType == UKOM || bimbelType == SKB_GIZI) ;
    }
}
