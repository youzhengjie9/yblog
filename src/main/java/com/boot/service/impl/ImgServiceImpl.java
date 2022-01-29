package com.boot.service.impl;

import com.boot.dao.ImgMapper;
import com.boot.pojo.Img;
import com.boot.service.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImgServiceImpl implements ImgService {

    @Autowired
    private ImgMapper imgMapper;


    @Override
    public void addImgPath(String big_img, String small_img) {

        imgMapper.addImgPath(big_img, small_img);

    }

    @Override
    public List<Img> selectAllImg() {
        return imgMapper.selectAllImg();
    }

    @Override
    public Img selectImgByid(int id) {
        return imgMapper.selectImgByid(id);
    }

    @Override
    public int selectImgCount() {
        return imgMapper.selectImgCount();
    }

    @Override
    public void deleteImgByid(int id) {
        imgMapper.deleteImgByid(id);
    }
}
