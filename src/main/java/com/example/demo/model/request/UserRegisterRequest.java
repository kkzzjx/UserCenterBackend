package com.example.demo.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: demo
 * @description:用户注册请求体
 * @author: zjx
 * @create: 2022-04-21 20:44
 **/
@Data   //lombok的注解 可以生成get/set方法
public class UserRegisterRequest implements Serializable{

    private String userAccount;
    private String password;
    private String checkPassword;

}
