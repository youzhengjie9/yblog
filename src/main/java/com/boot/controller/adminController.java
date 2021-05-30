package com.boot.controller;

import com.boot.data.ResponseData.ArticleResponseData;
import com.boot.pojo.*;
import com.boot.service.*;
import com.boot.utils.Commons;
import com.boot.utils.SpringSecurityUtil;
import com.boot.utils.bootstrap;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class adminController {


    private final String DEFAULT_CATEGORY="默认分类";

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

    @Autowired
    private categoryService categoryService;

    @Autowired
    private tagService tagService;

    @Autowired
    private userDetailService userDetailService;


    //初始化redis有关t_tag表的数据
    @PostConstruct
    public void initTags(){
        List<tag> tags = tagService.selectAllTag();

        for (tag tag : tags) {
            redisTemplate.opsForValue().set("tag_"+tag.getTagName(),tag.getTagCount());
        }

    }




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

        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail",userDetail);

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

        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail",userDetail);
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
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail",userDetail);
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
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail",userDetail);
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
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail",userDetail);
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
//            categoryService.updateCategoryCount(article.getCategories());
            java.util.Date date1 = new java.util.Date();
            long time = date1.getTime();
            Date date = new Date(time);
//          SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//          simpleDateFormat.format(date);
            article.setModified(date);

            //修改标签
            String pre_tags = articleService.selectTagsByArticleId(article.getId());
            String post_tags = article.getTags(); //修改之后的tags
            //1.先获取该文章的tags，对tag进行-1
            //2.再获取修改后的tags，对tag进行+1
            //3.再把post_tags覆盖到数据库的tags上
            String[] pre_split = pre_tags.split(",");
            String[] post_split = post_tags.split(",");

            for (String s : pre_split) {
                tagService.changeTagByTagNameDecr(s);
                redisTemplate.opsForValue().decrement("tag_"+s);
            }
            //如果没有这个某个标签，我们就把这个标签添加进去
            for (String s : post_split) {
                //通过redis判断有没有这个标签
                Integer o = (Integer) redisTemplate.opsForValue().get("tag_" + s);
                System.out.println("o====>"+o);
                if(o==null){
                    //如果缓存中没有这个标签就添加
                    tagService.insertTag(s);
                    //添加完数据库之后，我们还要把数据添加到redis缓存中
                    redisTemplate.opsForValue().set("tag_"+s,1);
                }else {
                    //如果缓存中有这个标签就+1
                    tagService.changeTagByTagNameIncr(s);
                    o++;
                    redisTemplate.opsForValue().set("tag_"+s,o);
                }

            }



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
            categoryService.updateCategoryCount(article.getCategories());
            article.setAllowComment(true);
            java.util.Date date1 = new java.util.Date();
            Date date = new Date(date1.getTime());
            article.setCreated(date);
            articleService.addArticle(article); //开启了 keyProperty="id" useGeneratedKeys="true"，自动生成的主键id会保存在article.getId()里。
            statisticService.addStatistic(new Statistic(article.getId(), 0, 0));

            String tags = article.getTags();
            String[] post_split = tags.split(",");
            //如果没有这个某个标签，我们就把这个标签添加进去
            for (String s : post_split) {
                //通过redis判断有没有这个标签
                Integer o = (Integer) redisTemplate.opsForValue().get("tag_" + s);
                if(o==null){
                    //如果缓存中没有这个标签就添加
                    tagService.insertTag(s);
                    //添加完数据库之后，我们还要把数据添加到redis缓存中
                    redisTemplate.opsForValue().set("tag_"+s,1);
                }else {
                    //如果缓存中有这个标签就+1
                    tagService.changeTagByTagNameIncr(s);
                    o++;
                    redisTemplate.opsForValue().set("tag_"+s,o);
                }

            }

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
            Article article = articleService.selectArticleByArticleIdNoComment(id);


            articleService.deleteArticleByArticleId(id);//删除文章
            commentService.deleteCommentByArticleId(id);//删除评论
            statisticService.deleteStatisticByArticleId(id);//删除点击量
            categoryService.updateCategoryCountDecr(article.getCategories());//修改分类数
            //修改标签
            String tags = article.getTags();
            String[] split = tags.split(",");
            if(split!=null&&split.length>0){
                for (String s : split) {
                    tagService.changeTagByTagNameDecr(s); //把每个标签-1
                    redisTemplate.opsForValue().decrement("tag_"+s); //把redis标签数-1
                }
            }

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

//    评论管理
    //跳转评论列表兼处理审核通过请求、删除请求
    @RequestMapping(path = "/toCommentList")
    public String toCommentList(@RequestParam(value = "delId",defaultValue = "-1") int delId,
                                @RequestParam(value = "id",defaultValue = "-1") int id,
                                @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                HttpSession session,
                                Model model) {
        String username = springSecurityUtil.currentUser(session);
        //处理审核
        if(id!=-1){
            try {
                commentService.checkSuccess(id);
            } catch (Exception e) {
                logger.error("修改评论失败。。id="+id);
                e.printStackTrace();
            }
        }

        //处理删除请求
        if(delId!=-1){
             //可以删除
            try {
                commentService.deleteComment(delId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        model.addAttribute("commons",Commons.getInstance());
        PageHelper.startPage(pageNum,5);
        List<Comment> comments = commentService.selectCommentsOrderCreateDate();
        PageInfo pageInfo = new PageInfo(comments);
        model.addAttribute("comments",comments);
        model.addAttribute("pageInfo",pageInfo);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail",userDetail);
        return "back/comment_list";
    }


//    //审核通过
//    @RequestMapping(path = "/checkSuccess")
//    public String checkSuccess(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum, Model model){
//
//
//
//
//        model.addAttribute("commons",Commons.getInstance());
//        PageHelper.startPage(pageNum,5);
//        List<Comment> comments = commentService.selectCommentsOrderCreateDate();
//        model.addAttribute("comments",comments);
//
//        return "back/comment_list";
//    }



    @RequestMapping(path = "/toTagList")
    public String toTagList(HttpSession session,Model model) {

        String username = springSecurityUtil.currentUser(session);

        List<category> categories = categoryService.selectCategories();
        List<tag> tags = tagService.selectAllTag();

        model.addAttribute("tags",tags);
        model.addAttribute("categories",categories);
        model.addAttribute("bootstrap",new bootstrap());
        model.addAttribute("commons",Commons.getInstance());
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail",userDetail);
        return "back/categories";
    }


    //修改分类
    @PostMapping(path = "/updateCategory")
    public String updateCategory(String oldName,String newName,HttpSession session,Model model){
        try {

            //修改分类表的分类名
            categoryService.updateCategory(oldName,newName);
            //修改article表中有关oldName的分类，修改成newName
            articleService.updateCategory(oldName, newName);
        }catch (Exception e){
            e.printStackTrace();
        }


        List<category> categories = categoryService.selectCategories();
        List<tag> tags = tagService.selectAllTag();

        model.addAttribute("tags",tags);
        model.addAttribute("categories",categories);
        model.addAttribute("bootstrap",new bootstrap());
        model.addAttribute("commons",Commons.getInstance());

        String username = springSecurityUtil.currentUser(session);

        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail",userDetail);

        return "back/categories";

    }

    @RequestMapping(path = "/deleteCategory")
    public String deleteCategory(@RequestParam(value = "n",defaultValue = "") String n,HttpSession session,Model model){
        if(n!=null&&!n.equals(""))
        {
            try{

                //把article的分类n改成默认分类
                articleService.updateCategory(n,"默认分类");
                //删除category表的分类n
                int count = categoryService.selectCategoryCountByName(n); //先查再删,获取删除分类的数量
                categoryService.deleteCategoryByName(n);
                //并且把删除的数量加到默认分类的数量上
                categoryService.updateCategoryCountByName(DEFAULT_CATEGORY,count);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        List<category> categories = categoryService.selectCategories();
        List<tag> tags = tagService.selectAllTag();

        model.addAttribute("tags",tags);
        model.addAttribute("categories",categories);
        model.addAttribute("bootstrap",new bootstrap());
        model.addAttribute("commons",Commons.getInstance());

        String username = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail",userDetail);

        return "back/categories";
    }


    @PostMapping(path = "/addCategory")
    public String addCategory(category category,HttpSession session,Model model){

        try {
            categoryService.addCategory(category);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<category> categories = categoryService.selectCategories();
        List<tag> tags = tagService.selectAllTag();

        model.addAttribute("tags",tags);
        model.addAttribute("categories",categories);
        model.addAttribute("bootstrap",new bootstrap());
        model.addAttribute("commons",Commons.getInstance());

        String username = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail",userDetail);

        return "back/categories";
    }




    @RequestMapping(path = "/toSetting")
    public String toSetting(HttpSession session,Model model) {

        model.addAttribute("commons",Commons.getInstance());
        model.addAttribute("bootstrap",new bootstrap());

        String username = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail",userDetail);

        return "back/setting";
    }









    //权限功能、秒杀产品服务







}
