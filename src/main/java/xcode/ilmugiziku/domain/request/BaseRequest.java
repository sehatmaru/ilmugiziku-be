package xcode.ilmugiziku.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseRequest {

    private String secureId;

    public BaseRequest() {
    }
}
