package xcode.ilmugiziku.domain.request.pack;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePackageRequest {
    private String title;
    private int price;
    private PackageFeatureRequest[] features;

    public CreatePackageRequest() {
    }
}
