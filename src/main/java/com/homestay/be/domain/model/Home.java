package com.homestay.be.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "home")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class Home {

   @Id @Column(name = "id",length = 36) @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private int id;

   @Column(name = "name", nullable = false, length = 50)
   private String name;
}
