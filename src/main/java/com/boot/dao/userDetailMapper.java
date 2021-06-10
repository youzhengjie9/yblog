package com.boot.dao;

import com.boot.pojo.userDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface userDetailMapper {

    //根据当前的用户名（唯一）去查找userDetail
    public userDetail selectUserDetailByUserName(@Param("name") String name);

    //个人资料，修改用户信息
    public void updateUserDetail(userDetail userDetail);



}
