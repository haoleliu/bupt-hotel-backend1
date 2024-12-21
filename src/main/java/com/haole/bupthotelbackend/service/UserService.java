package com.haole.bupthotelbackend.service;

import com.haole.bupthotelbackend.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author liu haole
* @description 针对表【user】的数据库操作Service
* @createDate 2024-12-20 14:13:49
*/
public interface UserService extends IService<User> {
    List<User> getUserById(Long userId);

    List<User> getUserByName(String name);

    User getUserByGenderOne(Integer gender);
}
