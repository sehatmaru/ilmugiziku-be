package xcode.ilmugiziku.domain.request.pack;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePackageRequest {
    private String title;
    private int price;
    private PackageFeatureRequest[] features;

    public UpdatePackageRequest() {
    }
}
