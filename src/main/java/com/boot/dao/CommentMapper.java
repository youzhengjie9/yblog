package com.boot.dao;

import com.boot.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentMapper {



    public List<Comment> SelectCommentsByArticleId(@Param("id") Integer id);

    public List<Comment> selectCommentsOrderCreateDate();

    public int deleteCommentByArticleId(@Param("id") Integer id);

    public int publishComment(Comment comment);

    public void checkSuccess(@Param("id") Integer id);

    public void deleteComment(@Param("id") Integer id);



}
