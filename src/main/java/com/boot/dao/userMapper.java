package com.boot.dao;

import com.boot.pojo.user;
import com.boot.pojo.user_authority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface userMapper {


    void addUser(user user);

    void addUserAuthority(user_authority user_authority);

    void updateEmail(@Param("email") String email);

    user selectUserInfoByuserName(@Param("username") String username);
}
