package xcode.ilmugiziku.domain.response.category;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CategoryResponse {
    private String secureId;
    private String title;
    private Date createdAt;
    private Date updatedAt;
}
