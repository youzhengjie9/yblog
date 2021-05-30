package com.boot.pojo;

import java.io.Serializable;

public class img implements Serializable {

    /**
     * 附件管理类
     */
    private int id;
    private String big_img; //大图
    private String small_img; //缩略图


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBig_img() {
        return big_img;
    }

    public void setBig_img(String big_img) {
        this.big_img = big_img;
    }

    public String getSmall_img() {
        return small_img;
    }

    public void setSmall_img(String small_img) {
        this.small_img = small_img;
    }

    @Override
    public String toString() {
        return "img{" +
                "id=" + id +
                ", big_img='" + big_img + '\'' +
                ", small_img='" + small_img + '\'' +
                '}';
    }
}
