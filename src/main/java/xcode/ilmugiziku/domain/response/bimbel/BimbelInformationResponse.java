package xcode.ilmugiziku.domain.response.bimbel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BimbelInformationResponse {
    private boolean isUkomActive;
    private boolean isSkbActive;

    public BimbelInformationResponse(boolean isUkomActive, boolean isSkbActive) {
        this.isUkomActive = isUkomActive;
        this.isSkbActive = isSkbActive;
    }
}
