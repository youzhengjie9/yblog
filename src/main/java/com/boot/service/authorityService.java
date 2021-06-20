package com.boot.service;

import com.boot.pojo.authority;
import com.boot.pojo.user_authority;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface authorityService {


    List<authority> selectUserAuthority();

    String selectAuthorityByid(int id);


}
