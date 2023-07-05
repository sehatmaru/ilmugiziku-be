package xcode.ilmugiziku.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import xcode.ilmugiziku.domain.enums.PackageTypeEnum;
import xcode.ilmugiziku.domain.enums.RegistrationTypeEnum;
import xcode.ilmugiziku.domain.enums.RoleEnum;

import javax.persistence.*;
import java.util.Date;

import static xcode.ilmugiziku.domain.enums.RoleEnum.ADMIN;

@Data
@Builder
@Entity
@Table(name = "t_user")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

   @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "secure_id")
   private String secureId;

   @Column(name = "email")
   private String email;

   @Column(name = "password")
   private String password;

   @Column(name = "type")
   private RegistrationTypeEnum type;

   @Column(name = "packages")
   private String packages;

   @Column(name = "role")
   private RoleEnum role;

   @Column(name = "active")
   private boolean active = false;

   @Column(name = "created_at")
   private Date createdAt;

   @Column(name = "updated_at")
   private Date updatedAt;

   @Column(name = "deleted_at")
   private Date deletedAt;

   public boolean isPremium() {
      return packages != null && !packages.isEmpty();
   }

   public boolean isUKOMPackage() {
      return packages != null && (packages.contains("1") || packages.contains("2"));
   }

   public boolean isSKBPackage() {
      return packages != null && (packages.contains("3") || packages.contains("4"));
   }

   public boolean isPaidPackage(PackageTypeEnum pack) {
      boolean result;

      if (pack == PackageTypeEnum.UKOM_NEWBIE) {
         if (isUKOMExpert()) {
            result = true;
         } else {
            result = isPremium() && packages.contains(String.valueOf(pack));
         }
      } else if (pack == PackageTypeEnum.SKB_NEWBIE) {
         if (isSKBExpert()) {
            result = true;
         } else {
            result = isPremium() && packages.contains(String.valueOf(pack));
         }
      } else {
         result = isPremium() && packages.contains(String.valueOf(pack));
      }

      return result;
   }

   public boolean isUKOMExpert() {
      return isPremium() && packages.contains("2");
   }

   public boolean isSKBExpert() {
      return isPremium() && packages.contains("4");
   }

   public boolean isAdmin() {
      return role == ADMIN;
   }
}
