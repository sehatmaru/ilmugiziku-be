package com.homestay.be.service;

import com.homestay.be.domain.model.Home;
import com.homestay.be.domain.repository.HomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeImpl implements HomeService {

   @Autowired
   HomeRepository homeRepository;

   @Override
   public List<Home> findAll() {
      return homeRepository.findAll();
   }

   @Override
   public Home findById(int id) {
      return homeRepository.findById(id);
   }

   @Override
   public Home update(int id, Home home) {
      home.getId();
      return homeRepository.save(home);
   }

   @Override
   public Home create(Home home) {
      return homeRepository.save(home);
   }

   @Override
   public void delete(int id) {
      homeRepository.deleteById(String.valueOf(id));
   }
}
