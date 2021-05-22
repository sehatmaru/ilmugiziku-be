package xcode.bookstore.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "books")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class Book {

   @Id @Column(name = "id",length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "title", nullable = false)
   private String title;

   @Column(name = "author", nullable = false)
   private String author;

   @Column(name = "publication", nullable = false)
   private String publication;

   @Column(name = "year", nullable = false)
   private String year;

   @Column(name = "created_at", nullable = false)
   private Date createdAt;

   @Column(name = "updated_at", nullable = false)
   private Date updatedAt;
}
