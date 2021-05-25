package xcode.bookstore.service;

import xcode.bookstore.domain.model.Book;
import xcode.bookstore.domain.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xcode.bookstore.controller.BookPresenter;

import java.util.List;

@Service
public class BookService implements BookPresenter {

   @Autowired
   BookRepository bookRepository;

   @Override
   public List<Book> findAll() {
      return bookRepository.findAll();
   }

   @Override
   public Book findById(int id) {
      return bookRepository.findById(id);
   }

   @Override
   public void update(int id, Book book) {
      bookRepository.save(book);
   }

   @Override
   public Book create(Book book) {
      return bookRepository.save(book);
   }

   @Override
   public boolean isBookExist(int id) {
      return bookRepository.findById(id) != null;
   }
}
