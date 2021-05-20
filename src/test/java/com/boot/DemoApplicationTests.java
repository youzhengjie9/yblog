package com.boot;

import com.boot.dao.CommentMapper;
import com.boot.dao.articleMapper;
import com.boot.pojo.Article;
import com.boot.pojo.Comment;
import com.boot.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private articleMapper articleMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentService service;



    @Test
    public void test(){
        System.out.println(service.hasComment(1));
        System.out.println(service.hasComment(2));
        System.out.println(service.hasComment(3));
    }







}
