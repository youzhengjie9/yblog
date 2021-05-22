package com.boot.service.impl;

import com.boot.dao.CommentMapper;
import com.boot.pojo.Comment;
import com.boot.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Override
    public Boolean hasComment(Integer id) {
        List<Comment> comment = commentMapper.SelectCommentsByArticleId(id);
        System.out.println(comment);
        if(comment==null||comment.size()==0){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public List<Comment> CommentByArticleId(Integer id) {
        return commentMapper.SelectCommentsByArticleId(id);
    }

    @Override
    public List<Comment> selectCommentsOrderCreateDate() {
        return commentMapper.selectCommentsOrderCreateDate();
    }

    @Override
    public int deleteCommentByArticleId(Integer id) {
        return commentMapper.deleteCommentByArticleId(id);
    }

    @Override
    public int publishComment(Comment comment) {
        return commentMapper.publishComment(comment);
    }
}
