package xcode.ilmugiziku.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TemplateResponse {
    private String secureId;
    private String name;
    private Date createdAt;
}
