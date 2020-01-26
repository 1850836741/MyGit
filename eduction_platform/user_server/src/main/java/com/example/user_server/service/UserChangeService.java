package com.example.user_server.service;

import com.example.user_server.entity.User;
import com.example.user_server.mapper.UserMapper;
import com.example.user_server.r_cache.RedisTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserChangeService {

    @Autowired
    RedisTool<User> redisTool;

    @Autowired(required = false)
    UserMapper userMapper;

    /**
     * 存储User的基本信息
     * @param user
     */
    public void addUser(User user){
        if (user == null) return;
        redisTool.deleteCacheByKey(String.valueOf(user.getUser_id()));
        userMapper.addUserBaseInformation(user);
    }

    /**
     * 删除用户
     * @param user_id
     */
    public void deleteUser(int user_id){
        if (user_id < 100000 || user_id > Integer.MAX_VALUE)return;
        redisTool.deleteCacheByKey(String.valueOf(user_id));
        userMapper.deleteUser(user_id);
    }

    /**
     * 修改用户信息
     * @param user
     */
    public void updateUser(User user){
        if (user == null) return;
        redisTool.deleteCacheByKey(String.valueOf(user.getUser_id()));
        userMapper.updateUser(user);
    }
}
