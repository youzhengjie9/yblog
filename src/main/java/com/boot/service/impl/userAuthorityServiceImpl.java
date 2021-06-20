package com.boot.service.impl;

import com.boot.dao.userAuthorityMapper;
import com.boot.pojo.user_authority;
import com.boot.service.userAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class userAuthorityServiceImpl implements userAuthorityService {

    @Autowired
    private userAuthorityMapper userAuthorityMapper;

    @Override
    public void changeUserAuthority(user_authority user_authority) {
        userAuthorityMapper.changeUserAuthority(user_authority);
    }

    @Override
    public int selectAuthorityID(int userid) {
        return userAuthorityMapper.selectAuthorityID(userid);
    }
}
