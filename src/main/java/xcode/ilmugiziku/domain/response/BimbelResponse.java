package xcode.ilmugiziku.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BimbelResponse {
    private List<WebinarResponse> webinars;
    private List<LessonResponse> lessons;

    public BimbelResponse() {
        webinars = new ArrayList<>();
        lessons = new ArrayList<>();
    }
}
