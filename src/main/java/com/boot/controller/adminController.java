package com.boot.controller;

import com.boot.data.ResponseData.ArticleResponseData;
import com.boot.pojo.Article;
import com.boot.pojo.Comment;
import com.boot.pojo.Statistic;
import com.boot.service.CommentService;
import com.boot.service.articleService;
import com.boot.service.statisticService;
import com.boot.utils.Commons;
import com.boot.utils.SpringSecurityUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
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

    //log4j
    private Logger logger = Logger.getLogger(adminController.class);

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @Autowired
    private RedisTemplate redisTemplate;


    @GetMapping(path = "/")
    public String toAdmin(Model model, HttpSession session) {
        String username = springSecurityUtil.currentUser(session);
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        logger.debug(time + "   用户名：" + username + "进入admim后台");
        PageHelper.startPage(1, 5);
        List<Article> articles = articleService.selectArticleOrderCreateDate();
        model.addAttribute("articles", articles);
        PageInfo articlePageInfo = new PageInfo(articles);
        model.addAttribute("articlePageInfo", articlePageInfo);
        PageHelper.startPage(1, 5);
        List<Comment> comments = commentService.selectCommentsOrderCreateDate();
        PageInfo commentPageInfo = new PageInfo(comments);
        model.addAttribute("commentPageInfo", commentPageInfo);
        model.addAttribute("comments", comments);
        model.addAttribute("commons", Commons.getInstance());
        int count = articleService.selectArticleCount();
        model.addAttribute("articleCount", count);


        return "back/index";
    }

    @RequestMapping(path = "/toPublishArticle")
    public String toPublishArticle(Model model, HttpSession session) {
        String username = springSecurityUtil.currentUser(session);
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        logger.debug(time + "   用户名：" + username + "进入后台发布页面");
        model.addAttribute("commons", Commons.getInstance());

        return "back/article_edit";
    }


    // 默认第一页
    @RequestMapping(path = "/toArticleList")
    public String toArticleList1(Model model, HttpSession session) {
        String username = springSecurityUtil.currentUser(session);
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        logger.debug(time + "   用户名：" + username + "查看文章列表");
        model.addAttribute("commons", Commons.getInstance());
        //此处我们也加了一层缓存,当有新文章发布时，我们就把这个key删除
//        List<Article> articles = (List<Article>) redisTemplate.opsForValue().get("articleList");
//        if(articles==null){
//            articles = articleService.selectAllArticleByCreated();
//            redisTemplate.opsForValue().set("articleList",articles);
//        }
        PageHelper.startPage(1, 6);
        List<Article> articles = articleService.selectAllArticleByCreated();
        PageInfo pageInfo = new PageInfo(articles);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("articles", articles);
        return "back/article_list";
    }


    @RequestMapping(path = "/toArticleList/{pageNum}")
    public String toArticleList2(@PathVariable("pageNum") Integer pageNum, Model model, HttpSession session) {
        String username = springSecurityUtil.currentUser(session);
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        logger.debug(time + "   用户名：" + username + "查看文章列表");
        model.addAttribute("commons", Commons.getInstance());
        //此处我们也加了一层缓存,当有新文章发布时，我们就把这个key删除
//        List<Article> articles = (List<Article>) redisTemplate.opsForValue().get("articleList");
//        if(articles==null){
//            articles = articleService.selectAllArticleByCreated();
//            redisTemplate.opsForValue().set("articleList",articles);
//        }
        PageHelper.startPage(pageNum, 6);
        List<Article> articles = articleService.selectAllArticleByCreated();
        PageInfo pageInfo = new PageInfo(articles);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("articles", articles);
        return "back/article_list";
    }

    @RequestMapping(path = "/toEditArticle/{article_id}")
    public String toEditArticle(@PathVariable("article_id") Integer article_id, HttpSession session, Model model) {
        String username = springSecurityUtil.currentUser(session);
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        logger.debug(time + "   用户名：" + username + "进入文章编辑页面");
        model.addAttribute("commons", Commons.getInstance());
        Article article = articleService.selectArticleByArticleIdNoComment(article_id);
        model.addAttribute("contents", article);
        return "back/article_edit";
    }


    @RequestMapping(path = "/modify")
    @ResponseBody //要加
    public ArticleResponseData modify(Article article, Model model, HttpSession session) {
        model.addAttribute("commons", Commons.getInstance());
        System.out.println("modify:" + article);
        List<Article> articles = articleService.selectAllArticleByCreated();
        model.addAttribute("articles", articles);
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
            //打印修改成功日志
            String username = springSecurityUtil.currentUser(session);
            java.util.Date date2 = new java.util.Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time2 = simpleDateFormat.format(date2);
            logger.debug(time2 + "   用户名：" + username + "修改文章信息成功，修改信息的JSON串为：" + article);
            return ArticleResponseData.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ArticleResponseData.fail();
        }

    }


    @RequestMapping(path = "/publish")
    @ResponseBody //要加
    public ArticleResponseData publish(Article article, HttpSession session, Model model) {
        model.addAttribute("commons", Commons.getInstance());
        System.out.println("publish:" + article);
        List<Article> articles = articleService.selectAllArticleByCreated();
        model.addAttribute("articles", articles);
        try {
            //发布操作代码
            article.setCategories("默认分类");
            article.setAllowComment(true);
            java.util.Date date1 = new java.util.Date();
            Date date = new Date(date1.getTime());
            article.setCreated(date);
            articleService.addArticle(article); //开启了 keyProperty="id" useGeneratedKeys="true"，自动生成的主键id会保存在article.getId()里。
            statisticService.addStatistic(new Statistic(article.getId(), 0, 0));
            String username = springSecurityUtil.currentUser(session);
            java.util.Date date2 = new java.util.Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(date2);
            logger.debug(time + "   用户名：" + username + "发布成功，发布信息的JSON串为：" + article);
//            redisTemplate.delete("articleList");
            return ArticleResponseData.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ArticleResponseData.fail();
        }
    }


    @RequestMapping(path = "/deleteArticle")
    @ResponseBody
    public ArticleResponseData deleteArticle(Integer id, HttpSession session) {

        try {
            articleService.deleteArticleByArticleId(id);
            commentService.deleteCommentByArticleId(id);
            statisticService.deleteStatisticByArticleId(id);
            String username = springSecurityUtil.currentUser(session);
            java.util.Date date = new java.util.Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(date);
            logger.debug(time + "   用户名：" + username + "删除文章成功，删除的文章id为：" + id);
//            redisTemplate.delete("articleList");
            return ArticleResponseData.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ArticleResponseData.fail();
        }
    }


    @RequestMapping(path = "/toCommentList")
    public String toCommentList(Model model) {

        model.addAttribute("commons",Commons.getInstance());

        return "back/comment_list";
    }

    @RequestMapping(path = "/toTagList")
    public String toTagList(Model model) {

        model.addAttribute("commons",Commons.getInstance());

        return "back/tag_list";
    }

    @RequestMapping(path = "/toSetting")
    public String toSetting(Model model) {

        model.addAttribute("commons",Commons.getInstance());
        return "back/setting";
    }


    //权限功能、秒杀产品服务







}
