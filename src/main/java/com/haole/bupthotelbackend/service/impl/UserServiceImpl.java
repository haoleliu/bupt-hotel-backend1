package com.haole.bupthotelbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haole.bupthotelbackend.model.domain.User;
import com.haole.bupthotelbackend.service.UserService;
import com.haole.bupthotelbackend.mapper.UserMapper;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author liu haole
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-12-20 14:13:49
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    public List<User> getUserById(Long userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        List<User> list = this.list(queryWrapper);
        return list;
    }

    public List<User> getUserByName(String userName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(User::getUsername, userName);
        List<User> list = this.list(queryWrapper);
        System.out.println(list);
        return this.list(queryWrapper);

    }

    @Override
    public User getUserByGenderOne(Integer gender) {
        try {
            return this.lambdaQuery().eq(User::getGender, gender)
                    .one();
        } catch (TooManyResultsException e) {
            e.printStackTrace();
        }
        return null;
    }

}




