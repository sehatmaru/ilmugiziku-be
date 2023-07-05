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
@Table(name = "t_profile")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class ProfileModel {
    
    @Id @Column(name = "id", length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "secure_id")
    private String secureId;

    @Column(name = "user_secure_id")
    private String user;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "email")
    private String email;

    @Column(name = "created_at")
    private Date createdAt;
 
    @Column(name = "updated_at")
    private Date updatedAt;
 
    @Column(name = "deleted_at")
    private Date deletedAt;

    public String getFullName() {
        return firstName + " " + lastName;
    }

}
