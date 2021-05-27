package com.boot.service.impl;

import com.boot.dao.userDetailMapper;
import com.boot.pojo.userDetail;
import com.boot.service.userDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class userDetailServiceImpl implements userDetailService {

    @Autowired
    private userDetailMapper userDetailMapper;

    @Override
    public userDetail selectUserDetailByUserName(String name) {
        return userDetailMapper.selectUserDetailByUserName(name);
    }
}
