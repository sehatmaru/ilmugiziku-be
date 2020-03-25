package com.homestay.be.controller;

import com.homestay.be.domain.model.Home;
import com.homestay.be.domain.response.BaseResponse;
import com.homestay.be.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "home")
public class HomeController {

    @Autowired
    HomeService homeService;

    @PostMapping
    ResponseEntity<BaseResponse> create (@RequestBody @Validated Home home) {
        BaseResponse response = new BaseResponse();
        response.setCode(200);
        response.setMessage("success");
        response.setData(homeService.create(home));

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
