package xcode.ilmugiziku.domain.response.pack;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackageFeatureResponse {
    private String secureId;
    private String desc;
    private boolean isAvailable;

    public PackageFeatureResponse(String secureId, String desc, boolean isAvailable) {
        this.secureId = secureId;
        this.desc = desc;
        this.isAvailable = isAvailable;
    }
}
