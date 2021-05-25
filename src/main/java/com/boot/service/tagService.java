package com.boot.service;

import com.boot.pojo.tag;
import org.apache.ibatis.annotations.Param;

public interface tagService {

    public void addTag(tag tag);

    //把指定标签的数量-1
    public void changeTagByTagNameDecr(String tagName);

    //把指定标签的数量+1
    public void changeTagByTagNameIncr(String tagName);
}
