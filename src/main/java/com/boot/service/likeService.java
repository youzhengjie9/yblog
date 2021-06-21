package com.boot.service;

import com.boot.pojo.like;
import org.apache.ibatis.annotations.Param;

public interface likeService {


    //点赞
    boolean likeArticle(like like);


}
