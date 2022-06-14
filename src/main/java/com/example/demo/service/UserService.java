package com.example.demo.service;

import com.example.demo.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 86133
* @description 针对表【user】的数据库操作Service 用户服务
* @createDate 2022-04-20 20:55:28
*/
public interface UserService extends IService<User> {



    /**
     * 用户注册
     * @param userAccount 用户账户
     * @param Password 用户密码
     * @param checkPassword 用户校验密码
     * @return 新用户id
     */
    long userRegister(String userAccount,String Password,String checkPassword);

    /**
     * 用户登录
     * @param userAccount 用户账户
     * @param Password 用户密码
     * @param request 请求
     * @return 登录的用户信息
     */
    User doLogin(String userAccount, String Password, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param user 原始用户
     * @return 脱敏后用户
     */

    User getSafeUser(User user);


    /**
     * 用户查找 （管理员权限）
     * @param username 用户名
     * @return 用户列表
     */
    List<User> searchUser(String username);

    /**
     * 用户删除（管理员权限）
     * @param id 用户id
     * @return 是佛删除成功
     */
    Boolean deleteUser(long id);

    /**
     * 用户注销（移除登录态）
     * @param request
     */
    int userLogout(HttpServletRequest request);




}
