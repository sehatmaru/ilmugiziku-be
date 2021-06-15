package xcode.ilmugiziku.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBookReq {
    private String title;
    private String author;
    private String publication;
    private String year;
    private int price;

    public CreateBookReq() {
    }
}
