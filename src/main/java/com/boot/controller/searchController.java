package com.boot.controller;

import com.boot.pojo.Article;
import com.boot.service.articleService;
import com.boot.utils.Commons;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
public class searchController {


    @Autowired
    private articleService articleService;

    @Autowired
    private RedisTemplate redisTemplate;


    //前10排行
    private static final List<Article> ArticleOrder_10(List<Article> articleList){
        List<Article> list=new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            list.add(articleList.get(i));
        }
        return list;
    }

    //搜索，到时要使用es进行搜索功能增强
    @RequestMapping(path = "/search")
    public ModelAndView search_article(String searchText){
//        PageHelper.startPage(1,5);


//        PageInfo pageInfo = new PageInfo(list);
        ModelAndView modelAndView = new ModelAndView();
        List<Article> as = (List<Article>) redisTemplate.opsForValue().get("articleOrders10");
        if(as==null){
            List<Article> articleOrders = ArticleOrder_10(articleService.selectAllArticleOrderByDesc());
            redisTemplate.opsForValue().set("articleOrders10",articleOrders,60*1, TimeUnit.SECONDS);
            modelAndView.addObject("articleOrders",articleOrders);
        }else {
            modelAndView.addObject("articleOrders",as);
        }
//        modelAndView.addObject("articles",list);
        modelAndView.addObject("commons", Commons.getInstance());
//        modelAndView.addObject("pageInfo",pageInfo);

        modelAndView.setViewName("client/index");
        return modelAndView;

    }


}
