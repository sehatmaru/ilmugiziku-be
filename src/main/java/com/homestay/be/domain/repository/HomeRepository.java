package com.homestay.be.domain.repository;

import com.homestay.be.domain.model.Home;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HomeRepository extends JpaRepository<Home, String> {

   Home findById(int id);

   Home findByName(String name);
}
