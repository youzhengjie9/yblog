package com.boot.dao;

import com.boot.pojo.user;
import com.boot.pojo.user_authority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface userMapper {


    void addUser(user user);

    void addUserAuthority(user_authority user_authority);

    void updateEmail(@Param("email") String email,@Param("username") String username);

    user selectUserInfoByuserName(@Param("username") String username);

    String selectPasswordByuserName(@Param("username") String username);

    void updatePassword(@Param("username") String username,@Param("password") String password);

    List<user> selectAllUser();

    //失效==valid变成0
    void updateValidTo_0(@Param("username") String username);

    //生效==valid变成1
    void updateValidTo_1(@Param("username") String username);

    void updateUser(@Param("id") int id,
                    @Param("email") String email);

}
