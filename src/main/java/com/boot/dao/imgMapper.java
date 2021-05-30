package com.boot.dao;

import com.boot.pojo.img;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface imgMapper {


    void addImgPath(@Param("big") String big_img , @Param("small") String small_img);

    List<img> selectAllImg();



}
