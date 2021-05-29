package com.boot.service.impl;

import com.boot.dao.userMapper;
import com.boot.pojo.user;
import com.boot.pojo.user_authority;
import com.boot.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class userServiceImpl implements userService {

    @Autowired
    private userMapper userMapper;

    @Override
    public void addUser(user user) {
        userMapper.addUser(user);
    }

    @Override
    public void addUserAuthority(user_authority user_authority) {
        userMapper.addUserAuthority(user_authority);
    }

    @Override
    public void updateEmail(String email) {
        userMapper.updateEmail(email);
    }

    @Override
    public user selectUserInfoByuserName(String username) {
        return userMapper.selectUserInfoByuserName(username);
    }


}
