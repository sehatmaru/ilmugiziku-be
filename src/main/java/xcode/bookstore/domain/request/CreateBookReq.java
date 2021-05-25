package xcode.bookstore.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBookReq {
    private String title;
    private String author;
    private String publication;
    private String year;

    public CreateBookReq() {
    }
}
