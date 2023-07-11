package xcode.ilmugiziku.domain.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PurchaseRequest {
    @NotBlank()
    private String successRedirectUrl;
    @NotBlank()
    private String failureRedirectUrl;

    public PurchaseRequest() {
    }

}
