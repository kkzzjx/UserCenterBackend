package com.example.demo.controller;

import com.example.demo.common.BaseResponse;
import com.example.demo.common.ErrorCode;
import com.example.demo.exception.BusinessException;
import com.example.demo.model.User;
import com.example.demo.model.request.UserRegisterRequest;
import com.example.demo.service.UserService;
import com.example.demo.common.ResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.constant.UserConstant.ADMIN_ROLE;
import static com.example.demo.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @program: demo
 * @description:
 * @author: zjx
 * @create: 2022-04-21 17:24
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){ //封装一个对象接收所有请求参数
        //RequestBody可以将前端传来的参数与对象作关联
        if(userRegisterRequest==null) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        //进行一个粗略的校验
        if(StringUtils.isAnyBlank(userAccount,password,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(userAccount, password, checkPassword);
       // return new BaseResponse<>(0,result,"ok");
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserRegisterRequest userRegisterRequest, HttpServletRequest httpServletRequest){
        if(userRegisterRequest==null) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String password = userRegisterRequest.getPassword();
        if(StringUtils.isAnyBlank(userAccount,password)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User user = userService.doLogin(userAccount, password, httpServletRequest);
      //  return new BaseResponse<>(0,user,"ok");
        if(user==null) throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名或密码错误，登录失败！");
        return ResultUtils.success(user);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        HttpSession session = request.getSession();
        Object userObj = session.getAttribute(USER_LOGIN_STATE);
        User currentUser=(User) userObj;
        if(currentUser == null) return null;

        /**
         * 需要考虑的问题：
         * 用户账户的状态会实时更新，因此如果直接返回session中存的用户信息，可能已经过期。
         * 所以可以考虑再查一遍数据库。
         */

        long userId=currentUser.getId();
        User originUser = userService.getById(userId);
        // todo: 校验用户是否合法
        // 不过这样还是有问题，要看一下用户状态（比如有无被封号）
        // Integer userStatus = originUser.getUserStatus();
        User safeUser = userService.getSafeUser(originUser);
        return ResultUtils.success(safeUser);
    }


    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username,HttpServletRequest request){
        //鉴权 仅管理员可查询
        if(!isAdmin(request)) return null;
    //    if(username==null) return null;
        List<User> userList = userService.searchUser(username);
        List<User> users = userList.stream().map(user -> userService.getSafeUser(user)).collect(Collectors.toList());
        return ResultUtils.success(users);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id,HttpServletRequest request){
        if(!isAdmin(request)) return ResultUtils.error(ErrorCode.PARAMS_NULL_ERROR);

        if(id<=0) return ResultUtils.error(ErrorCode.PARAMS_ERROR);
//        return userService.deleteUser(id);  //开启逻辑删除的话

//        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
//        queryWrapper.eq("id",id);
//        return userService.remove(queryWrapper);

        boolean result = userService.removeById(id);
        return ResultUtils.success(result);


    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if(request==null){
            return null;
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);


    }

    private boolean isAdmin(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user=(User) userObj;
        if(user == null||user.getUserRole()!=ADMIN_ROLE){
            return false;
        }
        return true;
    }
}
