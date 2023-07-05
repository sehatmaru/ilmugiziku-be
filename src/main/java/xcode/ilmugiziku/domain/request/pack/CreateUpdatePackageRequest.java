package xcode.ilmugiziku.domain.request.pack;

import lombok.Getter;
import lombok.Setter;
import xcode.ilmugiziku.domain.enums.PackageTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateUpdatePackageRequest {
    @NotBlank()
    private String title;
    @NotNull()
    private int price;
    private PackageTypeEnum packageType;
    private PackageFeatureRequest[] features;

    public CreateUpdatePackageRequest() {
    }
}
