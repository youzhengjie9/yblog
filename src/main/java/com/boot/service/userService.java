package com.boot.service;

import com.boot.pojo.user;
import com.boot.pojo.user_authority;
import org.apache.ibatis.annotations.Param;

public interface userService {


    void addUser(user user);

    void addUserAuthority(user_authority user_authority);

    void updateEmail(String email);

    user selectUserInfoByuserName(String username);
}
