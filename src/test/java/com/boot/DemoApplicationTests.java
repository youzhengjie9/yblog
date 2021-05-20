package com.boot;

import com.boot.dao.CommentMapper;
import com.boot.dao.articleMapper;
import com.boot.pojo.Article;
import com.boot.pojo.Comment;
import com.boot.service.CommentService;
import com.boot.utils.SpringSecurityUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private articleMapper articleMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentService service;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void Test(){
        Article article1= (Article) redisTemplate.opsForValue().get("article");
        System.out.println(article1);

    }





}
