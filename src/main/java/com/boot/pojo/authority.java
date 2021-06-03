package com.boot.pojo;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel(value = "权限实体类",description = "封装权限")
public class authority implements Serializable {

    private int id;
    private String authority;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "authority{" +
                "id=" + id +
                ", authority='" + authority + '\'' +
                '}';
    }
}
