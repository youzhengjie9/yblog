package com.boot.service;

import com.boot.pojo.user;
import com.boot.pojo.user_authority;
import org.apache.ibatis.annotations.Param;

public interface userService {


    void addUser(user user);

    void addUserAuthority(user_authority user_authority);

    void updateEmail(String email,String username);

    user selectUserInfoByuserName(String username);

    String selectPasswordByuserName(String username);

    void updatePassword(String username,String password);
}
