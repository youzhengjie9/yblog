package com.boot.service.impl;

import com.boot.dao.UserDetailMapper;
import com.boot.pojo.UserDetail;
import com.boot.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailService {

    @Autowired
    private UserDetailMapper userDetailMapper;

    @Override
    public UserDetail selectUserDetailByUserName(String name) {
        return userDetailMapper.selectUserDetailByUserName(name);
    }

    @Override
    public void updateUserDetail(UserDetail userDetail) {
        userDetailMapper.updateUserDetail(userDetail);
    }

    @Override
    public void addUserDetail(UserDetail userDetail) {
        userDetailMapper.addUserDetail(userDetail);
    }
}
