package xcode.ilmugiziku.domain.response.pack;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PackageResponse {
    private String secureId;
    private String title;
    private int price;
    private int packageType;
    private boolean isOpen;
    private List<PackageFeatureResponse> features;
}
