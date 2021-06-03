package com.boot.service.impl;

import com.boot.dao.authorityMapper;
import com.boot.pojo.authority;
import com.boot.pojo.user_authority;
import com.boot.service.authorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class authorityServiceImpl implements authorityService {
    @Autowired
    private authorityMapper authorityMapper;

    @Override
    public List<authority> selectUserAuthority() {
        return authorityMapper.selectUserAuthority();
    }
}
