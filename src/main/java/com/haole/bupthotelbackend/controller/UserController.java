package com.haole.bupthotelbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haole.bupthotelbackend.common.R;
import com.haole.bupthotelbackend.common.error;
import com.haole.bupthotelbackend.mapper.UserMapper;
import com.haole.bupthotelbackend.model.domain.User;
import com.haole.bupthotelbackend.request.UserLoginRequest;
import com.haole.bupthotelbackend.request.UserRegisterRequest;
import com.haole.bupthotelbackend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.haole.bupthotelbackend.constant.UserConstant.USER_LOGIN_STATE;


@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:8080","http://82.156.126.178:8080"}, allowCredentials = "true")
@Slf4j
public class UserController {


    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    @RequestMapping("/register2")
    public String register(String username,String userpassword,String confirmPassword,String Strid){
        int id=Integer.parseInt(Strid);
        return "OK";
    }
    @RequestMapping("/register")
    public String regiter2(UserRegisterRequest request){
        String password=request.getPassword();
        String username=request.getUsername();
        String confirmPassword=request.getConfirmPassword();
        Integer gender=request.getGender();
        if (!password.equals(confirmPassword)) return "ERROR";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setGender(gender);
        user.setCreatetime(new Date());
        user.setUpdatetime(new Date());
        userService.save(user);
        return user.toString();
    }

    @RequestMapping("/login")
    public R<User> userLogin(@RequestBody UserLoginRequest request, HttpServletRequest httpServletRequest){
        String username=request.getUsername();
        String password=request.getPassword();
        List<User> list;
        list=userService.lambdaQuery().eq(User::getUsername,username).list();
        if (list.size()==0) return R.fail("用户不存在");
        User resultUser=list.get(0);
        String TruePassword=list.get(0).getPassword();
        if (password.equals(TruePassword)) {
            System.out.println(resultUser);
            httpServletRequest.getSession().setAttribute(USER_LOGIN_STATE,resultUser);
            return R.ok("登录成功",resultUser);
        }
        log.info("查询不到用户，username={}，password={}",username,password);
        return R.fail("用户名或密码错误");
    }

    @RequestMapping("/monitor/login")
    public R<User> monitorLogin(@RequestBody UserLoginRequest request,HttpServletRequest httpServletRequest){
        String username=request.getUsername();
        String password=request.getPassword();
        List<User> list;
        list=userService.lambdaQuery().eq(User::getUsername,username).list();
        if (list.size()==0) return R.fail("用户不存在");
        User resultUser=list.get(0);
        if (resultUser.getAccount()==0){
            return R.fail("权限不足");
        }
        String TruePassword=list.get(0).getPassword();
        if (password.equals(TruePassword)) {
            System.out.println(resultUser);
            httpServletRequest.getSession().setAttribute(USER_LOGIN_STATE,resultUser);
            return R.ok("登录成功",resultUser);
        }
        log.info("查询不到用户，username={}，password={}",username,password);
        return R.fail("用户名或密码错误");
    }


}
