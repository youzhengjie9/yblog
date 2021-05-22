package com.boot.controller;

import com.boot.pojo.Article;
import com.boot.service.articleService;
import com.boot.service.elasticSearchService;
import com.boot.utils.Commons;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
public class searchController {


    @Autowired
    private articleService articleService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private elasticSearchService elasticSearchService;


    //前10排行
    private static final List<Article> ArticleOrder_10(List<Article> articleList){
        List<Article> list=new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            list.add(articleList.get(i));
        }
        return list;
    }

    //搜索，到时要使用es进行搜索功能增强
    @RequestMapping(path = "/search/{page}")
    public ModelAndView search_article(@PathVariable("page") int page, String searchText) throws IOException {

        ModelAndView modelAndView = new ModelAndView();

        //搜索内容为空代表搜素全部
        if(searchText==null||searchText.equals("")){
            PageHelper.startPage(1,5);
            List<Article> list = articleService.selectAllArticle();
            PageInfo pageInfo = new PageInfo(list);

            modelAndView.addObject("articles",list);
            modelAndView.addObject("pageInfo",pageInfo);
        }else {
            //es搜索
            SearchHit[] searchHits = elasticSearchService.searchArticleGetHits(searchText);
            List<Article> articles = elasticSearchService.getArticleListByHits(searchHits);


            PageInfo pageInfo = new PageInfo(articles);

            modelAndView.addObject("articles",articles);
            modelAndView.addObject("pageInfo",pageInfo);
        }

        List<Article> as = (List<Article>) redisTemplate.opsForValue().get("articleOrders10");
        if(as==null){
            List<Article> articleOrders = ArticleOrder_10(articleService.selectAllArticleOrderByDesc());
            redisTemplate.opsForValue().set("articleOrders10",articleOrders,60*1, TimeUnit.SECONDS);
            modelAndView.addObject("articleOrders",articleOrders);
        }else {
            modelAndView.addObject("articleOrders",as);
        }
        modelAndView.addObject("commons", Commons.getInstance());


        modelAndView.setViewName("client/index");
        return modelAndView;

    }


}