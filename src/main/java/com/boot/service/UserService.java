package com.boot.service;

import com.boot.pojo.User;
import com.boot.pojo.UserAuthority;

import java.util.List;

public interface UserService {


    void addUser(User user);

    void addUserAuthority(UserAuthority user_authority);

    void updateEmail(String email,String username);

    User selectUserInfoByuserName(String username);



    String selectPasswordByuserName(String username);

    void updatePassword(String username,String password);

    List<User> selectAllUser();

    //失效==valid变成0
    void updateValidTo_0(String username);

    //生效==valid变成1
    void updateValidTo_1(String username);

    void updateUserForEmail(int id,
                    String email);

    int selectUseridByUserName(String username);

    int userCount();

    //根据用户名和email去查询用户
    List<User> selectUserByUsernameAndEmail(String username, String email);

    //根据用户名和email去查询用户数量
    int selectUserCountByUsernameAndEmail(String username,String email);

}
