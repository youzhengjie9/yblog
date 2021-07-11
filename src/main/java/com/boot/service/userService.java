package com.boot.service;

import com.boot.pojo.user;
import com.boot.pojo.user_authority;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface userService {


    void addUser(user user);

    void addUserAuthority(user_authority user_authority);

    void updateEmail(String email,String username);

    user selectUserInfoByuserName(String username);

    String selectPasswordByuserName(String username);

    void updatePassword(String username,String password);

    List<user> selectAllUser();

    //失效==valid变成0
    void updateValidTo_0(String username);

    //生效==valid变成1
    void updateValidTo_1(String username);

    void updateUserForEmail(int id,
                    String email);

    int selectUseridByUserName(String username);

    int userCount();

    //根据用户名和email去查询用户
    List<user> selectUserByUsernameAndEmail(String username,String email);

    //根据用户名和email去查询用户数量
    int selectUserCountByUsernameAndEmail(String username,String email);

}
