package com.boot.service;

import com.boot.pojo.UserDetail;

public interface UserDetailService {

    //根据当前的用户名（唯一）去查找userDetail
    public UserDetail selectUserDetailByUserName(String name);

    //个人资料，修改用户信息
    public void updateUserDetail(UserDetail userDetail);

    public void addUserDetail(UserDetail userDetail);
}
