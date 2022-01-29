package com.boot.dao;

import com.boot.pojo.Img;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ImgMapper {


    void addImgPath(@Param("big") String big_img , @Param("small") String small_img);

    List<Img> selectAllImg();

    Img selectImgByid(@Param("id") int id);

    int selectImgCount();

    void deleteImgByid(@Param("id") int id);
}
