package xcode.bookstore.service;

import xcode.bookstore.domain.model.Book;

import java.util.List;

public interface BookService {
   List<Book> findAll();

   Book findById(int id);

   Book update(int id, Book book);

   Book create(Book book);

   void delete(int id);
}
