package com.homestay.be.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse<T> {
    private int code;
    private String message;
    private T data;
}
