package com.boot.service.impl;

import com.boot.dao.UserAuthorityMapper;
import com.boot.pojo.UserAuthority;
import com.boot.service.UserAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAuthorityServiceImpl implements UserAuthorityService {

    @Autowired
    private UserAuthorityMapper userAuthorityMapper;

    @Override
    public void changeUserAuthority(UserAuthority user_authority) {
        userAuthorityMapper.changeUserAuthority(user_authority);
    }

    @Override
    public int selectAuthorityID(int userid) {
        return userAuthorityMapper.selectAuthorityID(userid);
    }
}
