package xcode.bookstore.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xcode.bookstore.domain.model.Book;
import xcode.bookstore.domain.request.CreateBookReq;
import xcode.bookstore.domain.response.BaseResponse;
import xcode.bookstore.controller.BookPresenter;

import java.util.Date;
import java.util.List;

import static xcode.bookstore.shared.BaseResponseConst.*;

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

    @GetMapping("/detail/{id}")
    ResponseEntity<BaseResponse<Book>> getDetail(@PathVariable(value = "id") int id) {
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

    @DeleteMapping("/delete/{id}")
    ResponseEntity<BaseResponse<Integer>> delete(@PathVariable(value = "id") int id) {
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

    @PutMapping("/update/{id}")
    ResponseEntity<BaseResponse<Book>> update (@PathVariable(value = "id") int id, @RequestBody @Validated CreateBookReq request) {
        BaseResponse<Book> response = new BaseResponse<>();

        if (bookPresenter.isBookExist(id)) {
            Book book = bookPresenter.findById(id);
            book.setTitle(request.getTitle());
            book.setPublication(request.getPublication());
            book.setAuthor(request.getAuthor());
            book.setYear(request.getYear());
            book.setUpdatedAt(new Date());

            try {
                bookPresenter.update(id, book);

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
