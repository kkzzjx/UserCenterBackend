package com.example.demo.exception;

import com.example.demo.common.ErrorCode;

/**
 * @program: demo
 * @description: 业务的自定义异常
 * @author: zjx
 * @create: 2022-05-01 19:55
 **/
public class BusinessException extends RuntimeException{
    private int code;
    private String description;

    public BusinessException(String message,int code,String description){
        super(message);
        this.code=code;
        this.description=description;

    }

    public BusinessException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.code=errorCode.getCode();
        this.description=errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode,String description){
        super(errorCode.getMessage());
        this.code=errorCode.getCode();
        this.description=description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
