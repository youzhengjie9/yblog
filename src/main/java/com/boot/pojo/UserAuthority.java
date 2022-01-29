package com.boot.pojo;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "用户权限实体类",description = "封装每个用户对应的权限")
public class UserAuthority {

    private int user_id; //用户id
    private int authority_id; //所拥有的权限id

    public UserAuthority() {
    }

    public UserAuthority(int user_id, int authority_id) {
        this.user_id = user_id;
        this.authority_id = authority_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAuthority_id() {
        return authority_id;
    }

    public void setAuthority_id(int authority_id) {
        this.authority_id = authority_id;
    }

    @Override
    public String toString() {
        return "user_authority{" +
                "user_id=" + user_id +
                ", authority_id=" + authority_id +
                '}';
    }
}
