package com.example.demo.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: demo
 * @description:通用返回类
 * @author: zjx
 * @create: 2022-04-26 23:09
 **/
@Data
public class BaseResponse<T> implements Serializable {
    //泛型 提高这个方法的可复用性
    private int code;
    private T data;
    private String message;
    private String description;

    public BaseResponse(int code, T data, String message,String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description=description;


    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(),null,errorCode.getMessage(),errorCode.getDescription());
    }

    public BaseResponse(ErrorCode errorCode,String message,String description) {
        this(errorCode.getCode(),null,errorCode.getMessage(),errorCode.getDescription());
    }


}
