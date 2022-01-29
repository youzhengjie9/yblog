package com.boot.service;

import com.boot.pojo.Like;

public interface LikeService {


    //点赞
    boolean likeArticle(Like like);


    String selectLikeExsit(String username,int articleid);
}
