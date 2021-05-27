package com.boot.service;

import com.boot.pojo.userDetail;
import org.apache.ibatis.annotations.Param;

public interface userDetailService {

    //根据当前的用户名（唯一）去查找userDetail
    public userDetail selectUserDetailByUserName(String name);



}
