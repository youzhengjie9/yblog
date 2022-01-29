package com.boot.controller;

import com.boot.constant.ThemeConstant;
import com.boot.pojo.Article;
import com.boot.pojo.Link;
import com.boot.pojo.Tag;
import com.boot.service.*;
import com.boot.utils.Commons;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
/**
 * @author 游政杰
 * 2021/5/22 15:54
 */
@Controller
@Api("全局搜索控制器")
public class SearchController {


    @Autowired
    private ArticleService articleService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private LinkService linkService;

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Autowired
    private LikeService likeService;


    @Autowired
    private TagService tagService;

    //前10排行
    private static final List<Article> ArticleOrder_10(List<Article> articleList) {
        List<Article> list = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            list.add(articleList.get(i));
        }
        return list;
    }

    //搜索，到时要使用es进行搜索功能增强
    @RequestMapping(path = "/search/{page}")
    public ModelAndView search_article(@PathVariable("page") int page, String searchText) throws IOException {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("articleService",articleService);
        modelAndView.addObject("likeService",likeService);
        //跳转不同页面主题判断
        if (ThemeConstant.curTheme.equals(ThemeConstant.CALM_THEME)){ //calm主题
            modelAndView.setViewName("client/index2"); //跳转页面
            modelAndView.addObject("indexAc","active");
            List<Tag> tags = tagService.selectTags_limit8();
            modelAndView.addObject("tags",tags);

        }else if(ThemeConstant.curTheme.equals(ThemeConstant.DEFAULT_THEME)){ //默认主题
            modelAndView.setViewName("client/index"); //跳转页面

        }

        //搜索内容为空代表搜素全部
        if (searchText == null || searchText.equals("")) {
            PageHelper.startPage(1, 5);
            List<Article> list = articleService.selectAllArticle();
            PageInfo pageInfo = new PageInfo(list);

            modelAndView.addObject("articles", list);
            modelAndView.addObject("pageInfo", pageInfo);
        } else {
            //es搜索
            SearchHit[] searchHits = elasticSearchService.searchArticleGetHits(searchText);
            List<Article> articles = elasticSearchService.getArticleListByHits(searchHits);


            PageInfo pageInfo = new PageInfo(articles);

            modelAndView.addObject("articles", articles);
            modelAndView.addObject("pageInfo", pageInfo);
        }

        List<Article> as = (List<Article>) redisTemplate.opsForValue().get("articleOrders10");
        if (as == null) {
            List<Article> articleOrders = ArticleOrder_10(articleService.selectAllArticleOrderByDesc());
            redisTemplate.opsForValue().set("articleOrders10", articleOrders, 60 * 1, TimeUnit.SECONDS);
            modelAndView.addObject("articleOrders", articleOrders);
        } else {
            modelAndView.addObject("articleOrders", as);
        }
        modelAndView.addObject("commons", Commons.getInstance());

        //友链
        List<Link> links = linkService.selectAllLink();
        modelAndView.addObject("links",links);
        return modelAndView;

    }


}
