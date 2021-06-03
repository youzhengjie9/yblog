package com.boot.pojo;

import java.io.Serializable;

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
