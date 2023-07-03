package xcode.ilmugiziku.domain.request.pack;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackageFeatureRequest {
    private String secureId;
    private boolean isAvailable;

    public PackageFeatureRequest() {
    }
}
