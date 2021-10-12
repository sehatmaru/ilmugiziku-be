package xcode.ilmugiziku.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.request.SubmitRatingRequest;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.domain.response.BimbelResponse;
import xcode.ilmugiziku.domain.response.pack.PackageResponse;
import xcode.ilmugiziku.presenter.BimbelPresenter;
import xcode.ilmugiziku.presenter.PackagePresenter;
import xcode.ilmugiziku.presenter.RatingPresenter;

import java.util.List;

@RestController
@RequestMapping(value = "bimbel")
public class BimbelApi {

    final PackagePresenter packagePresenter;
    final RatingPresenter ratingPresenter;
    final BimbelPresenter bimbelPresenter;

    public BimbelApi(PackagePresenter packagePresenter,
                     RatingPresenter ratingPresenter,
                     BimbelPresenter bimbelPresenter) {
        this.packagePresenter = packagePresenter;
        this.ratingPresenter = ratingPresenter;
        this.bimbelPresenter = bimbelPresenter;
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

    @PostMapping("/lesson/rating/set")
    ResponseEntity<BaseResponse<Double>> submitRating(
            @RequestParam @Validated String token,
            @RequestParam @Validated String lessonSecureId,
            @RequestBody @Validated SubmitRatingRequest body
    ) {
        BaseResponse<Double> response = ratingPresenter.submitRating(token, lessonSecureId, body);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/lesson")
    ResponseEntity<BaseResponse<BimbelResponse>> getBimbelPackage(
            @RequestParam @Validated String token,
            @RequestParam @Validated int bimbelType
    ) {
        BaseResponse<BimbelResponse> response = bimbelPresenter.getBimbelPackage(token, bimbelType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
