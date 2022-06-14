package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.ErrorCode;
import com.example.demo.exception.BusinessException;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.demo.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author 86133
* @description 用户服务实现类
* @createDate 2022-04-20 20:55:28
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {
    @Resource
    UserMapper userMapper;

    /**
     * 盐值 用于混淆密码
     */
    private static final String SALT="kkzzjx";



    @Override
    public long userRegister(String userAccount, String Password, String checkPassword) {

        //1. 校验
        if(StringUtils.isAnyBlank(userAccount,Password,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }

        if(userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账户过短");
        }
        if(Password.length()<8||checkPassword.length()<8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码过短");
        }


        //账户不包含特殊字符
        String validPattern=  "\".*[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*()——+|{}【】‘；：”“’。，、？\\\\\\\\]+.*\"";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
//        if(matcher.matches()) System.out.println("test");
//        else return -1;
        if(matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if(!Password.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //账户不能重复

        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
      //  long count = this.count(queryWrapper);
        long count = userMapper.selectCount(queryWrapper);
        if(count>0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 2. 密码加密

        String md5Password = DigestUtils.md5DigestAsHex((SALT + Password).getBytes());

        // 3.插入数据

        User user=new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(md5Password);
       // boolean saveResult = this.save(user);
        int saveResult = userMapper.insert(user);
        if(saveResult<1) return -1;
        return user.getId();

    }

    @Override
    public User doLogin(String userAccount, String Password, HttpServletRequest request){

        //1. 校验
        if(StringUtils.isAnyBlank(userAccount,Password)){
//            // todo 修改为自定义异常
//            return null; //这个返回需要优化 封装异常类
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if(userAccount.length()<4){
         //   return null;
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(Password.length()<8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }


        //账户不包含特殊字符
        String validPattern=  "\".*[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*()——+|{}【】‘；：”“’。，、？\\\\\\\\]+.*\"";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
//        if(matcher.matches()) System.out.println("test");
//        else return -1;
        if(matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //账户是否存在



        // 2. 校验密码
        String md5Password = DigestUtils.md5DigestAsHex((SALT + Password).getBytes());
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",md5Password);
        //先要看用户是否存在 安全性考虑：还要做限流，放在一个用户请求登录性过多
        User user = userMapper.selectOne(queryWrapper);
        if(user==null){
            log.info("user login failed,userAccount cannot match userPassword");
            return null;  //也是返回密码错误
        }
//        String truePassword = user.getUserPassword();
//        if(!md5Password.equals(truePassword)) return null; //密码错误

        // 3. 用户脱敏
        User safetyUser=getSafeUser(user);


        // 4. 记录用户的登录态
        HttpSession session = request.getSession();
        session.setAttribute(USER_LOGIN_STATE,user);

        // todo: 用redis改造成分布式登录  等优化ahhh


        return safetyUser;

    }

    /**
     * 用户脱敏
     * @param user 原始用户
     * @return 安全用户
     */

    @Override
    public User getSafeUser(User user){
        User safetyUser=new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setPlanetCode(user.getPlanetCode());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        return safetyUser;
    }



    @Override
    public List<User> searchUser(String username) {
        if(username==null) return null;

        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.like("username",username);
        List<User> userList = userMapper.selectList(queryWrapper);
        return userList;
    }

    @Override
    public Boolean deleteUser(long id){
        if(id<0) return false;
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",id);
        if(queryWrapper==null) return false;
        userMapper.delete(queryWrapper);
        return true;


    }

    /**
     * 用户注销
     * @param request
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;

    }
}




