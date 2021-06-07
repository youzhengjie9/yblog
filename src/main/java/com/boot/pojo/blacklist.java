package com.boot.pojo;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel(value = "黑名单")
public class blacklist implements Serializable {

    private int id;
    private String black_ip;

    public blacklist() {
    }

    public blacklist(int id, String black_ip) {
        this.id = id;
        this.black_ip = black_ip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBlack_ip() {
        return black_ip;
    }

    public void setBlack_ip(String black_ip) {
        this.black_ip = black_ip;
    }

    @Override
    public String toString() {
        return "blacklist{" +
                "id=" + id +
                ", black_ip='" + black_ip + '\'' +
                '}';
    }
}
