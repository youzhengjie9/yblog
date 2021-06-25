package com.boot.service;

import com.boot.pojo.tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface tagService {

    public void addTag(tag tag);

    //把指定标签的数量-1
    public void changeTagByTagNameDecr(String tagName);

    //把指定标签的数量+1
    public void changeTagByTagNameIncr(String tagName);

    public List<tag> selectAllTag();

    public void insertTag(String tagName);

    //echarts
    List<tag> selectTags_echarts();

    //首页标签
    List<tag> selectTags_limit8();

    int selectTagCount();
}
