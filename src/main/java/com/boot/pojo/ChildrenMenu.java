package com.boot.pojo;


import io.swagger.annotations.ApiModel;

import java.util.Arrays;
import java.util.List;

@ApiModel("后台系统子菜单")
public class ChildrenMenu {

    private int id;
    private String title;
    private String icon;
    private int type;
    private String openType;
    private String href;
    //注意：子菜单可能还会有子菜单，也就是多级菜单
    private ChildrenMenu[] children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public ChildrenMenu[] getChildren() {
        return children;
    }

    public void setChildren(ChildrenMenu[] children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "ChildrenMenu{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", icon='" + icon + '\'' +
                ", type=" + type +
                ", openType='" + openType + '\'' +
                ", href='" + href + '\'' +
                ", children=" + Arrays.toString(children) +
                '}';
    }
}
