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
@Table(name = "t_auth_token")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokenModel {

   @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "token")
   private String token;

   @Column(name = "auth_secure_id")
   private String authSecureId;

   @Column(name = "created_at")
   private Date createdAt;

   @Column(name = "updated_at")
   private Date updatedAt;

   @Column(name = "deleted_at")
   private Date deletedAt;

   public AuthTokenModel(String token, String authSecureId) {
      this.token = token;
      this.authSecureId = authSecureId;
      this.createdAt = new Date();
   }
}
