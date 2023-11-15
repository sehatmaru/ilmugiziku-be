package xcode.marsiajar.domain.request.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUpdateCategoryRequest {
    private String title;

    public CreateUpdateCategoryRequest() {
    }
}
