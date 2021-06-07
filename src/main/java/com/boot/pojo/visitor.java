package com.boot.pojo;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel(value = "访问者信息",description = "获取访问者的所有信息")
public class visitor implements Serializable {

    private int id;
    private String visit_ip; //访问者ip
    private String visit_address; //访问者ip所在地址
    private String browser; //访问者使用的浏览器
    private String os; //访问者使用的操作系统
    private String visit_time; //访问时间
    private String visit_describe; //访问描述


    public visitor() {
    }


    public visitor(int id, String visit_ip, String visit_address, String browser, String os, String visit_time, String visit_describe) {
        this.id = id;
        this.visit_ip = visit_ip;
        this.visit_address = visit_address;
        this.browser = browser;
        this.os = os;
        this.visit_time = visit_time;
        this.visit_describe = visit_describe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVisit_ip() {
        return visit_ip;
    }

    public void setVisit_ip(String visit_ip) {
        this.visit_ip = visit_ip;
    }

    public String getVisit_address() {
        return visit_address;
    }

    public void setVisit_address(String visit_address) {
        this.visit_address = visit_address;
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

    public String getVisit_time() {
        return visit_time;
    }

    public void setVisit_time(String visit_time) {
        this.visit_time = visit_time;
    }

    public String getVisit_describe() {
        return visit_describe;
    }

    public void setVisit_describe(String visit_describe) {
        this.visit_describe = visit_describe;
    }

    @Override
    public String toString() {
        return "visitor{" +
                "id=" + id +
                ", visit_ip='" + visit_ip + '\'' +
                ", visit_address='" + visit_address + '\'' +
                ", browser='" + browser + '\'' +
                ", os='" + os + '\'' +
                ", visit_time='" + visit_time + '\'' +
                ", visit_describe='" + visit_describe + '\'' +
                '}';
    }
}
