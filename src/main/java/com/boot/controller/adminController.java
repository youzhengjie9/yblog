package com.boot.controller;

import com.boot.data.ResponseData.ArticleResponseData;
import com.boot.pojo.Article;
import com.boot.pojo.Comment;
import com.boot.pojo.Statistic;
import com.boot.service.CommentService;
import com.boot.service.articleService;
import com.boot.service.statisticService;
import com.boot.utils.Commons;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class adminController {

    @Autowired
    private articleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private statisticService statisticService;


    @GetMapping(path = "/")
    public String toAdmin(Model model){


        PageHelper.startPage(1,5);
        List<Article> articles = articleService.selectArticleOrderCreateDate();
        model.addAttribute("articles",articles);
        PageInfo articlePageInfo = new PageInfo(articles);
        model.addAttribute("articlePageInfo",articlePageInfo);
        PageHelper.startPage(1,5);
        List<Comment> comments = commentService.selectCommentsOrderCreateDate();
        PageInfo commentPageInfo = new PageInfo(comments);
        model.addAttribute("commentPageInfo",commentPageInfo);
        model.addAttribute("comments",comments);
        model.addAttribute("commons",new Commons());
        int count = articleService.selectArticleCount();
        model.addAttribute("articleCount",count);


        return "back/index";
    }

    @RequestMapping(path = "/toPublishArticle")
    public String toPublishArticle(Model model){

        model.addAttribute("commons",new Commons());

        return "back/article_edit";
    }

    @RequestMapping(path = "/toArticleList")
    public String toArticleList(Model model){
        model.addAttribute("commons",new Commons());
        List<Article> articles = articleService.selectAllArticleByCreated();
        model.addAttribute("articles",articles);
        return "back/article_list";
    }

    @RequestMapping(path = "/toEditArticle/{article_id}")
    public String toEditArticle(@PathVariable("article_id") Integer article_id, Model model){
        model.addAttribute("commons",new Commons());
        Article article = articleService.selectArticleByArticleIdNoComment(article_id);
        model.addAttribute("contents",article);
        return "back/article_edit";
    }


    @RequestMapping(path = "/modify")
    @ResponseBody //要加
    public ArticleResponseData modify(Article article,Model model){
        model.addAttribute("commons",new Commons());
        System.out.println("modify:"+article);
        List<Article> articles = articleService.selectAllArticleByCreated();
        model.addAttribute("articles",articles);
        try {
            //修改操作代码
            article.setCategories("默认分类");
            java.util.Date date1 = new java.util.Date();
            long time = date1.getTime();
            Date date = new Date(time);
//          SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//          simpleDateFormat.format(date);
            article.setModified(date);
            articleService.changeArticle(article);
            return ArticleResponseData.ok();
        }catch (Exception e){
            e.printStackTrace();
            return ArticleResponseData.fail();
        }

    }



    @RequestMapping(path = "/publish")
    @ResponseBody //要加
    public ArticleResponseData publish(Article article, Model model){
        model.addAttribute("commons",new Commons());
        System.out.println("publish:"+article);
        List<Article> articles = articleService.selectAllArticleByCreated();
        model.addAttribute("articles",articles);
        try {
            //发布操作代码
            article.setCategories("默认分类");
            article.setAllowComment(true);
            java.util.Date date1 = new java.util.Date();
            Date date = new Date(date1.getTime());
            article.setCreated(date);
            articleService.addArticle(article); //开启了 keyProperty="id" useGeneratedKeys="true"，自动生成的主键id会保存在article.getId()里。
            statisticService.addStatistic(new Statistic(article.getId(),0,0));
            return ArticleResponseData.ok();
        }catch (Exception e){
            e.printStackTrace();
            return ArticleResponseData.fail();
        }
    }



    @RequestMapping(path = "/deleteArticle")
    @ResponseBody
    public ArticleResponseData deleteArticle(Integer id){
        try {
            articleService.deleteArticleByArticleId(id);
            commentService.deleteCommentByArticleId(id);
            statisticService.deleteStatisticByArticleId(id);
            return ArticleResponseData.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ArticleResponseData.fail();
        }
    }


}
