package xcode.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xcode.bookstore.domain.model.Book;
import xcode.bookstore.domain.response.BaseResponse;
import xcode.bookstore.service.BookService;

@RestController
@RequestMapping(value = "book")
public class BookController {

    @Autowired
    BookService bookService;

    @PostMapping("/create")
    ResponseEntity<BaseResponse> create (@RequestBody @Validated Book request) {
        BaseResponse response = new BaseResponse();
        response.setCode(200);
        response.setMessage("success");
        response.setData(bookService.create(request));

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
