package com.boot.dao;

import com.boot.pojo.tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface tagMapper {


    public void addTag(tag tag);

    //把指定标签的数量-1
    public void changeTagByTagNameDecr(@Param("tagName") String tagName);

    //把指定标签的数量+1
    public void changeTagByTagNameIncr(@Param("tagName") String tagName);

    public void insertTag(@Param("tagName") String tagName);

}
