package com.boot.pojo;

import io.swagger.annotations.ApiModel;

import java.util.Arrays;
import java.util.List;

/**
 * @author 游政杰
 */
@ApiModel("后台系统的主菜单")
public class Menu {

    private int id;
    private String title;
    private int type;
    private String icon;
    private String href;
    private ChildrenMenu[] children; //子菜单


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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
        return "Menu{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", icon='" + icon + '\'' +
                ", href='" + href + '\'' +
                ", children=" + Arrays.toString(children) +
                '}';
    }
}
