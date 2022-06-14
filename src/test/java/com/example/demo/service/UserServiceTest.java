package com.example.demo.service;
import java.util.Date;

import com.example.demo.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
/**
 * 用户服务测试
 *
 * @Author kkzzjx
 * **/

@SpringBootTest
class UserServiceTest {
    @Resource
    UserService userService;

    @Test
    public void testAddUser(){
        User user=new User();
        user.setUsername("kk");
        user.setUserAccount("123");
        user.setAvatarUrl("https://kk.com");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("123");
        user.setEmail("123@qq.com");
        user.setUserStatus(0);
        boolean result = userService.save(user);

        System.out.println(user.getId());
        Assertions.assertEquals(true,result);  //ctrl+p 提示参数

    }

    @Test
    void userRegister() {
//        String userAccount="kkzzjx1234";
//        String password="abc123456";
//        String checkPassword="abc123456";
//
//        long result = userService.userRegister(userAccount, password, checkPassword);
//
//        Assertions.assertEquals(2,result);


        String userAccount="kkzzjx1234";
        String password="abc123456";
        String checkPassword="abc123456";
        long result = userService.userRegister(userAccount, password, checkPassword);

        Assertions.assertEquals(-1,result);
    }
}