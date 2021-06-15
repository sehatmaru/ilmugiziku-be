package xcode.ilmugiziku.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "book")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class Book {

   @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "title")
   private String title;

   @Column(name = "author")
   private String author;

   @Column(name = "publication")
   private String publication;

   @Column(name = "year")
   private String year;

   @Column(name = "price")
   private int price;

   @Column(name = "created_at")
   private Date createdAt;

   @Column(name = "updated_at")
   private Date updatedAt;

   @Column(name = "deleted_at")
   private Date deletedAt;
}
