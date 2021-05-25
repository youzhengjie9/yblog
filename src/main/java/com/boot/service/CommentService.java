package com.boot.service;


import com.boot.pojo.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentService {


    public Boolean hasComment(Integer id);

    public List<Comment> CommentByArticleId(@Param("id") Integer id);

    public List<Comment> selectCommentsOrderCreateDate();

    public int deleteCommentByArticleId(Integer id);

    public int publishComment(Comment comment);

    public void checkSuccess(Integer id);

    public void deleteComment(Integer id);

}
