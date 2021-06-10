package com.boot.pojo;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel(value = "黑名单")
public class blacklist implements Serializable {

    private int id;
    private String black_ip; //黑名单Ip
    private String black_address; //黑名单Ip对应的地址

    public blacklist() {
    }

    public blacklist(int id, String black_ip, String black_address) {
        this.id = id;
        this.black_ip = black_ip;
        this.black_address = black_address;
    }

    public int getId() {
        return id;
    }


    public String getBlack_ip() {
        return black_ip;
    }

    public void setBlack_ip(String black_ip) {
        this.black_ip = black_ip;
    }

    public String getBlack_address() {
        return black_address;
    }

    public void setBlack_address(String black_address) {
        this.black_address = black_address;
    }

    @Override
    public String toString() {
        return "blacklist{" +
                "id=" + id +
                ", black_ip='" + black_ip + '\'' +
                ", black_address='" + black_address + '\'' +
                '}';
    }
}
