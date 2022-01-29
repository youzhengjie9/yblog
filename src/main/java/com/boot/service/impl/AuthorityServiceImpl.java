package com.boot.service.impl;

import com.boot.dao.AuthorityMapper;
import com.boot.pojo.Authority;
import com.boot.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    private AuthorityMapper authorityMapper;

    @Override
    public List<Authority> selectUserAuthority() {
        return authorityMapper.selectUserAuthority();
    }

    @Override
    public String selectAuthorityByid(int id) {
        return authorityMapper.selectAuthorityByid(id);
    }
}
