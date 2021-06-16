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
@Table(name = "auth")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class AuthModel {

   @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "secure_id")
   private String secureId;

   @Column(name = "name")
   private String name;

   @Column(name = "email")
   private String email;

   @Column(name = "password")
   private String password;

   @Column(name = "type")
   private int type;

   @Column(name = "role")
   private int role;

   @Column(name = "created_at")
   private Date createdAt;

   @Column(name = "updated_at")
   private Date updatedAt;

   @Column(name = "deleted_at")
   private Date deletedAt;
}
