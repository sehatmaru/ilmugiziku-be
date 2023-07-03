package xcode.ilmugiziku.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmitRatingRequest {
    private int rating;

    public SubmitRatingRequest() {
    }
}
