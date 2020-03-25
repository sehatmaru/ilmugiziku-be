package com.homestay.be.service;

import com.homestay.be.domain.model.Home;

import java.util.List;

public interface HomeService {
   List<Home> findAll();

   Home findById(int id);

   Home update(int id, Home home);

   Home create(Home home);

   void delete(int id);
}
