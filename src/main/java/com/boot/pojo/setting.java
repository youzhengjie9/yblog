package com.boot.pojo;

import io.swagger.annotations.ApiModel;

@ApiModel("系统设置")
public class setting {

    private int id;
    private String name; //用户名
    private String logo; //博客logo
    private String foot; //页尾内容
    private String theme; //主题

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getFoot() {
        return foot;
    }

    public void setFoot(String foot) {
        this.foot = foot;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    @Override
    public String toString() {
        return "setting{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", foot='" + foot + '\'' +
                ", theme='" + theme + '\'' +
                '}';
    }
}
