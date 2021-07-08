package com.boot.pojo;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel("登录日志")
public class loginLog implements Serializable {

    private int id;
    private String username; //登录的用户名
    private String ip; //登录的ip
    private String address; //ip对应的地址
    private String browser; //登录使用的浏览器
    private String os; //登录使用的操作系统
    private String time; //登录时间
    private int type; //登录类型： 1是正常登录  2是记住我自动登录

    public int getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "loginLog{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", ip='" + ip + '\'' +
                ", address='" + address + '\'' +
                ", browser='" + browser + '\'' +
                ", os='" + os + '\'' +
                ", time='" + time + '\'' +
                ", type=" + type +
                '}';
    }
}
