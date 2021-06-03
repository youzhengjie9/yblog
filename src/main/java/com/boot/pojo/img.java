package com.boot.pojo;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel(value = "上传的附件图片实体类",description = "封装大图和缩略图的路径")
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
