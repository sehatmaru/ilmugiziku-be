package xcode.ilmugiziku.domain.response.bimbel;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.response.LessonResponse;
import xcode.ilmugiziku.domain.response.WebinarResponse;

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
