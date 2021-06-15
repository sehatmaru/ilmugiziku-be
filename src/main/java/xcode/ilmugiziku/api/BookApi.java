package xcode.ilmugiziku.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.ilmugiziku.domain.model.Book;
import xcode.ilmugiziku.domain.request.CreateBookReq;
import xcode.ilmugiziku.domain.response.BaseResponse;
import xcode.ilmugiziku.controller.BookPresenter;

import java.util.Date;
import java.util.List;

import static xcode.ilmugiziku.shared.BaseResponseConst.*;

@RestController
@RequestMapping(value = "book")
public class BookApi {

    @Autowired
    BookPresenter bookPresenter;

    @PostMapping("/create")
    ResponseEntity<BaseResponse<Integer>> create (@RequestBody @Validated CreateBookReq request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPublication(request.getPublication());
        book.setYear(request.getYear());
        book.setPrice(request.getPrice());
        book.setCreatedAt(new Date());

        BaseResponse<Integer> response = new BaseResponse<>();

        try {
            Book newBook = bookPresenter.create(book);

            response.setCode(SUCCESS_CODE);
            response.setMessage(SUCCESS_MESSAGE);

            response.setData(newBook.getId());
        } catch (Exception e) {
            response.setCode(FAILED_CODE);
            response.setMessage(e.toString());
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/detail")
    ResponseEntity<BaseResponse<Book>> getDetail(@RequestParam @Validated int id) {
        BaseResponse<Book> response = new BaseResponse<>();

        if (bookPresenter.isBookExist(id)) {
            response.setCode(SUCCESS_CODE);
            response.setMessage(SUCCESS_MESSAGE);
            response.setData(bookPresenter.findById(id));
        } else {
            response.setCode(NOT_FOUND_CODE);
            response.setMessage(NOT_FOUND_MESSAGE);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @DeleteMapping("/delete")
    ResponseEntity<BaseResponse<Integer>> delete(@RequestParam @Validated int id) {
        BaseResponse<Integer> response = new BaseResponse<>();

        if (bookPresenter.isBookExist(id)) {
            Book book = bookPresenter.findById(id);
            book.setDeletedAt(new Date());

            try {
                bookPresenter.update(id, book);

                response.setCode(SUCCESS_CODE);
                response.setMessage(SUCCESS_MESSAGE);
                response.setData(id);
            } catch (Exception e) {
                response.setCode(FAILED_CODE);
                response.setMessage(e.toString());
            }
        } else {
            response.setCode(NOT_FOUND_CODE);
            response.setMessage(NOT_FOUND_MESSAGE);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/update")
    ResponseEntity<BaseResponse<Book>> update (@RequestBody @Validated Book request) {
        BaseResponse<Book> response = new BaseResponse<>();

        if (bookPresenter.isBookExist(request.getId())) {
            Book book = bookPresenter.findById(request.getId());
            book.setTitle(request.getTitle());
            book.setPublication(request.getPublication());
            book.setAuthor(request.getAuthor());
            book.setYear(request.getYear());
            book.setPrice(request.getPrice());
            book.setUpdatedAt(new Date());

            try {
                bookPresenter.update(request.getId(), book);

                response.setCode(SUCCESS_CODE);
                response.setMessage(SUCCESS_MESSAGE);
                response.setData(book);
            } catch (Exception e) {
                response.setCode(FAILED_CODE);
                response.setMessage(e.toString());
            }
        } else {
            response.setCode(NOT_FOUND_CODE);
            response.setMessage(NOT_FOUND_MESSAGE);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("/list")
    ResponseEntity<BaseResponse<List<Book>>> getAll () {
        List<Book> books;

        BaseResponse<List<Book>> response = new BaseResponse<>();

        try {
            books = bookPresenter.findAll();

            response.setCode(SUCCESS_CODE);
            response.setMessage(SUCCESS_MESSAGE);
            response.setData(books);
        } catch (Exception e) {
            response.setCode(FAILED_CODE);
            response.setMessage(e.toString());
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
