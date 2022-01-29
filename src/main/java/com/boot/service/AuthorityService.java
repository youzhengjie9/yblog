package com.boot.service;

import com.boot.pojo.Authority;

import java.util.List;

public interface AuthorityService {


    List<Authority> selectUserAuthority();

    String selectAuthorityByid(int id);


}
