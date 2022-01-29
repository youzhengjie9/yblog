package com.boot.service;

import com.boot.pojo.Tag;

import java.util.List;

public interface TagService {

    public void addTag(Tag tag);

    //把指定标签的数量-1
    public void changeTagByTagNameDecr(String tagName);

    //把指定标签的数量+1
    public void changeTagByTagNameIncr(String tagName);

    public List<Tag> selectAllTag();

    public void insertTag(String tagName);

    //echarts
    List<Tag> selectTags_echarts();

    //首页标签
    List<Tag> selectTags_limit8();

    int selectTagCount();
}
