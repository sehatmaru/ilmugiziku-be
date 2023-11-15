package xcode.marsiajar.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xcode.marsiajar.domain.response.BaseResponse;
import xcode.marsiajar.domain.response.category.CategoryResponse;
import xcode.marsiajar.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping(value = "common")
public class CommonApi {

    @Autowired private CategoryService categoryService;

    @GetMapping("/category/list")
    ResponseEntity<BaseResponse<List<CategoryResponse>>> getCategoryList() {
        BaseResponse<List<CategoryResponse>> response = categoryService.getCategoryList("");

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
