package com.boot.service;

import com.boot.pojo.Img;

import java.util.List;

public interface ImgService {


    void addImgPath(String big_img ,String small_img);

    //从数据库查找所有图片地址
    List<Img> selectAllImg();

    Img selectImgByid(int id);

    int selectImgCount();

    void deleteImgByid(int id);
}
