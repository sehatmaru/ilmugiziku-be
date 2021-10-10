package xcode.ilmugiziku.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.pack.PackageResponse;
import xcode.ilmugiziku.presenter.PackagePresenter;

import java.util.List;

@RestController
@RequestMapping(value = "bimbel")
public class BimbelApi {

    final PackagePresenter packagePresenter;

    public BimbelApi(PackagePresenter packagePresenter) {
        this.packagePresenter = packagePresenter;
    }

    @GetMapping("/package/list")
    ResponseEntity<BaseResponse<List<PackageResponse>>> list(
            @RequestParam @Validated String token
    ) {
        BaseResponse<List<PackageResponse>> response = packagePresenter.getPackageList(token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
