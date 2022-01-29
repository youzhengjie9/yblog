package com.boot.dao;

import com.boot.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TagMapper {


    public void addTag(Tag tag);

    //把指定标签的数量-1
    public void changeTagByTagNameDecr(@Param("tagName") String tagName);

    //把指定标签的数量+1
    public void changeTagByTagNameIncr(@Param("tagName") String tagName);

    public void insertTag(@Param("tagName") String tagName);

    public List<Tag> selectAllTag();

    //echarts
    List<Tag> selectTags_echarts();

    //首页标签
    @Select("select * from t_tag  order by tagCount desc limit 8")
    List<Tag> selectTags_limit8();

    @Select("select count(*) from t_tag")
    int selectTagCount();


}
