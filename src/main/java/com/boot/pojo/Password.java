package com.boot.pojo;

import io.swagger.annotations.Api;

import java.io.Serializable;

@Api("封装修改密码的信息")
public class Password implements Serializable {


    private String oldPassword; //旧密码

    private String newPassword; //新密码

    private String checkPassword; //确认密码


    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getCheckPassword() {
        return checkPassword;
    }

    public void setCheckPassword(String checkPassword) {
        this.checkPassword = checkPassword;
    }

    @Override
    public String toString() {
        return "password{" +
                "oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", checkPassword='" + checkPassword + '\'' +
                '}';
    }
}
