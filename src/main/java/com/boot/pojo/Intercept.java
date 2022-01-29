package com.boot.pojo;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel("封装拦截记录类")
public class Intercept implements Serializable {

    private int id;
    private String intercept_ip; //拦截ip
    private String intercept_address; //拦截ip所在地址
    private String intercept_browser; //拦截ip的人使用的浏览器
    private String intercept_os; //拦截ip的人使用的操作系统
    private String intercept_time; //拦截时间
    private String desc; //描述

    public int getId() {
        return id;
    }


    public String getIntercept_ip() {
        return intercept_ip;
    }

    public void setIntercept_ip(String intercept_ip) {
        this.intercept_ip = intercept_ip;
    }

    public String getIntercept_address() {
        return intercept_address;
    }

    public void setIntercept_address(String intercept_address) {
        this.intercept_address = intercept_address;
    }

    public String getIntercept_browser() {
        return intercept_browser;
    }

    public void setIntercept_browser(String intercept_browser) {
        this.intercept_browser = intercept_browser;
    }

    public String getIntercept_os() {
        return intercept_os;
    }

    public void setIntercept_os(String intercept_os) {
        this.intercept_os = intercept_os;
    }

    public String getIntercept_time() {
        return intercept_time;
    }

    public void setIntercept_time(String intercept_time) {
        this.intercept_time = intercept_time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "intercept{" +
                "id=" + id +
                ", intercept_ip='" + intercept_ip + '\'' +
                ", intercept_address='" + intercept_address + '\'' +
                ", intercept_browser='" + intercept_browser + '\'' +
                ", intercept_os='" + intercept_os + '\'' +
                ", intercept_time='" + intercept_time + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
