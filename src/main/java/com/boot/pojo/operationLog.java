package com.boot.pojo;


import java.io.Serializable;

public class operationLog implements Serializable {

    private int id;
    private String username; //操作的用户名
    private String ip; //操作的ip
    private String address; //操作的ip对应的地址
    private String browser; //使用的浏览器
    private String os; //使用的操作系统
    private String time; //操作时间
    private String type; //操作了什么

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "operationLog{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", ip='" + ip + '\'' +
                ", address='" + address + '\'' +
                ", browser='" + browser + '\'' +
                ", os='" + os + '\'' +
                ", time='" + time + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
