package com.boot.service.impl;

import com.boot.dao.imgMapper;
import com.boot.pojo.img;
import com.boot.service.imgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class imgServiceImpl implements imgService {

    @Autowired
    private imgMapper imgMapper;


    @Override
    public void addImgPath(String big_img, String small_img) {

        imgMapper.addImgPath(big_img, small_img);

    }

    @Override
    public List<img> selectAllImg() {
        return imgMapper.selectAllImg();
    }

    @Override
    public img selectImgByid(int id) {
        return imgMapper.selectImgByid(id);
    }

    @Override
    public int selectImgCount() {
        return imgMapper.selectImgCount();
    }
}
