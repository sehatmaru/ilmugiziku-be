package xcode.bookstore.domain.repository;

import xcode.bookstore.domain.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

   Book findById(int id);
}
