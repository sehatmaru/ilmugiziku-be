package xcode.ilmugiziku.controller;

import xcode.ilmugiziku.domain.model.Book;

import java.util.List;

public interface BookPresenter {
   List<Book> findAll();

   Book findById(int id);

   void update(int id, Book book);

   Book create(Book book);

   boolean isBookExist(int id);
}
