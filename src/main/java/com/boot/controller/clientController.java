package com.boot.controller;

import com.boot.annotation.Visitor;
import com.boot.constant.themeConstant;
import com.boot.dao.articleMapper;
import com.boot.data.ResponseData.ArticleResponseData;
import com.boot.pojo.*;
import com.boot.service.*;
import com.boot.utils.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.util.concurrent.RateLimiter;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.boot.utils.browserOS.*;

/**
 * @author 游政杰
 * 2021/5/20 17:08
 */
@Controller
@Api("客户端界面控制器")
public class clientController {

    @Value("${server.port}")
    private String port;

    @Autowired
    private articleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private SpringSecurityUtil securityUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private userDetailService userDetailService;

    @Autowired
    private linkService linkService;

    @Autowired
    private browserOS browserOS;

    @Autowired
    private visitorService visitorService;

    private final int DEFAULT_PAGENUM = 1;

    private final int COMMENT_PAGESIZE = 3;

    private final int type = 1;

    @Autowired
    private tagService tagService;


    @Autowired
    private settingService settingService;

    @Autowired
    private likeService likeService;

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    private static RateLimiter rateLimiter=RateLimiter.create(20); //令牌桶实现，提升系统吞吐量



    //前10排行
    private static final List<Article> ArticleOrder_10(List<Article> articleList) {
        List<Article> list = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            list.add(articleList.get(i));
        }
        return list;
    }

    private void queryUserDeail(HttpSession session, ModelAndView modelAndView) {
        SecurityContextImpl securityContext = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
        if (securityContext != null) {
            String name = securityUtil.currentUser(session);
            if (name != null && !name.equals("")) {
                userDetail userDetail = userDetailService.selectUserDetailByUserName(name);
                modelAndView.addObject("userDetail", userDetail);
            }
        } else {
            userDetail userDetail = null;
            modelAndView.addObject("userDetail", userDetail);
        }
    }

    private void setting(HttpSession session,ModelAndView modelAndView){
        SecurityContextImpl securityContext = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
        if (securityContext != null) {
            String name = securityUtil.currentUser(session);
            setting setting = settingService.selectUserSetting(name);
            modelAndView.addObject("setting",setting);
        }else {
            modelAndView.addObject("setting",null);
        }

    }


    @Visitor(desc = "访问首页")
    @RequestMapping(path = {"/"})
    public ModelAndView toIndex1(HttpSession session, HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView();

        if(!rateLimiter.tryAcquire()){
            System.out.println("当前系统访问人数较多，请稍后访问");
             modelAndView.setViewName("back/newback/article/limit");
             return modelAndView;
        }

//        System.out.println("测试负载均衡==当前端口是："+port);

        //传setting给前端
        this.setting(session,modelAndView);

        modelAndView.addObject("articleService",articleService);
        modelAndView.addObject("likeService",likeService);
        SecurityContextImpl securityContext = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
        if (securityContext != null) {
            String name = securityUtil.currentUser(session);
            if (name != null && !name.equals("")) {
                modelAndView.addObject("user",name);
            }
        } else {
            modelAndView.addObject("user",null);
        }

        //跳转不同页面主题判断
        if (themeConstant.curTheme.equals(themeConstant.CALM_THEME)) { //calm主题
            modelAndView.setViewName("client/index2"); //跳转页面
            List<tag> tags = tagService.selectTags_limit8();
            modelAndView.addObject("indexAc", "active");
            modelAndView.addObject("tags", tags);

        } else if (themeConstant.curTheme.equals(themeConstant.DEFAULT_THEME)) { //默认主题
            modelAndView.setViewName("client/index"); //跳转页面

        }


        PageHelper.startPage(1, 8);
        List<Article> list = articleService.selectAllArticle();
        PageInfo pageInfo = new PageInfo(list);

        List<Article> as = (List<Article>) redisTemplate.opsForValue().get("articleOrders10");
        if (as == null) {
            List<Article> articleOrders = ArticleOrder_10(articleService.selectAllArticleOrderByDesc());
            redisTemplate.opsForValue().set("articleOrders10", articleOrders, 60 * 1, TimeUnit.SECONDS);
            modelAndView.addObject("articleOrders", articleOrders);
        } else {
            modelAndView.addObject("articleOrders", as);
        }

        /**
         * xxx个人博客标题
         */
        this.queryUserDeail(session, modelAndView);


        //友链
        List<link> links = linkService.selectAllLink();
        modelAndView.addObject("links", links);

        //推荐文章
        PageHelper.startPage(1,5);
        List<Article> recommends = articleService.selectArticleByRecommend();
        modelAndView.addObject("recommends",recommends);

        modelAndView.addObject("articles", list);
        modelAndView.addObject("commons", Commons.getInstance());
        modelAndView.addObject("pageInfo", pageInfo);

        return modelAndView;
    }


    @Visitor(desc = "访问首页")
    @RequestMapping(path = {"/page/{pageNum}"})
    public ModelAndView toIndex2(@PathVariable("pageNum") int pageNum, HttpSession session, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();

        //传setting给前端
        this.setting(session,modelAndView);

        modelAndView.addObject("articleService",articleService);
        modelAndView.addObject("likeService",likeService);
        SecurityContextImpl securityContext = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
        if (securityContext != null) {
            String name = securityUtil.currentUser(session);
            if (name != null && !name.equals("")) {
                modelAndView.addObject("user",name);
            }
        } else {
            modelAndView.addObject("user",null);
        }

        //跳转不同页面主题判断
        if (themeConstant.curTheme.equals(themeConstant.CALM_THEME)) { //calm主题
            modelAndView.setViewName("client/index2"); //跳转页面
            modelAndView.addObject("indexAc", "active");
            List<tag> tags = tagService.selectTags_limit8();
            modelAndView.addObject("tags", tags);

        } else if (themeConstant.curTheme.equals(themeConstant.DEFAULT_THEME)) { //默认主题
            modelAndView.setViewName("client/index"); //跳转页面

        }

        PageHelper.startPage(pageNum, 8);
        List<Article> list = articleService.selectAllArticle();
        PageInfo pageInfo = new PageInfo(list);

        List<Article> as = (List<Article>) redisTemplate.opsForValue().get("articleOrders10");
        if (as == null) {
            List<Article> articleOrders = ArticleOrder_10(articleService.selectAllArticleOrderByDesc());
            redisTemplate.opsForValue().set("articleOrders10", articleOrders, 60 * 1, TimeUnit.SECONDS);
            modelAndView.addObject("articleOrders", articleOrders);
        } else {
            modelAndView.addObject("articleOrders", as);
        }

        /**
         * xxx个人博客标题
         */
        this.queryUserDeail(session, modelAndView);


        //友链
        List<link> links = linkService.selectAllLink();
        modelAndView.addObject("links", links);

        //推荐文章
        PageHelper.startPage(1,5);
        List<Article> recommends = articleService.selectArticleByRecommend();
        modelAndView.addObject("recommends",recommends);

        modelAndView.addObject("articles", list);
        modelAndView.addObject("commons", Commons.getInstance());
        modelAndView.addObject("pageInfo", pageInfo);

        return modelAndView;
    }


    @Visitor(desc = "访问文章")
    @GetMapping(path = "/article/{articleId}")
    public ModelAndView toArticleDetailByID(@PathVariable("articleId") Integer articleId, HttpServletRequest request, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();

        //传setting给前端
        this.setting(session,modelAndView);

        //当某个ip在短时间内不断访问某篇文章会造成点击量+1
        //需求：我们想让用户ip访问文章后2分钟内，再次点击这篇文章点击量不会+1，防止用户刷点击量
        String ipAddr = ipUtils.getIpAddr(request); //1.先获取ip
        String key = "ip_" + ipAddr + "_ar_" + articleId;
        Object o = redisTemplate.opsForValue().get(key);//2.通过ip和文章id，去查询有没有对应的值
        if (o == null) {
            //文章点击数+1
            articleService.updateHits(articleId);
            redisTemplate.opsForValue().set(key, "1", 60 * 2, TimeUnit.SECONDS); //设置2分钟的过期时间
        }


        //传入
        modelAndView.addObject("indexAc", "active");


        boolean res = false; //判断是否传入参数“c”
        Boolean hasComment = commentService.hasComment(articleId);
        Article article = null;


        /**
         * xxx个人博客标题
         */
        SecurityContextImpl securityContext = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
        if (securityContext != null) {
            String name = securityUtil.currentUser(session);
            if (name != null && !name.equals("")) {
                userDetail userDetail = userDetailService.selectUserDetailByUserName(name);
                modelAndView.addObject("name", name);
                modelAndView.addObject("userDetail", userDetail);
            }
        } else {
            userDetail userDetail = null;
            modelAndView.addObject("userDetail", userDetail);
        }


        //从Redis数据库中获取指定文章id的数据，如果没有就从数据库查询，然后在放入redis中
        article = (Article) redisTemplate.opsForValue().get("articleId_" + articleId);
        if (article == null) {
            article = articleService.selectArticleByArticleIdNoComment(articleId);//查文章内容（没有评论）
            redisTemplate.opsForValue().set("articleId_" + articleId, article);
        }
        String c = request.getParameter("c");
        int pageNum = 0;
        if (c == null || c.equals("")) {
            res = false;
        } else {
            try {
                pageNum = Integer.parseInt(c);
                res = true;
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                res = false;
            }
        }
        if (res) {
            if (hasComment) {
                PageHelper.startPage(pageNum, COMMENT_PAGESIZE);
                List<Comment> comments = commentService.CommentByArticleId(articleId); //一定是要mybatis查询才行，get方法获取不行
                PageInfo pageInfo = new PageInfo(comments);
                modelAndView.addObject("pageInfo", pageInfo);
                modelAndView.addObject("comments", comments);
            }
            modelAndView.addObject("article", article);
            modelAndView.addObject("commons", Commons.getInstance());
            modelAndView.addObject("articleId", articleId);
            if (themeConstant.curTheme.equals(themeConstant.CALM_THEME)) { //calm主题
                modelAndView.setViewName("client/articleDetails2"); //跳转页面

            } else if (themeConstant.curTheme.equals(themeConstant.DEFAULT_THEME)) { //默认主题
                modelAndView.setViewName("client/articleDetails"); //跳转页面
            }
            return modelAndView;


        } else {
            if (hasComment) {
//                List<Comment> comments = (List<Comment>) redisTemplate.opsForValue().get("cm_aid_" + articleId + "_n_" + pageNum + "_s_" + COMMENT_PAGESIZE);
//                if(comments==null){
//                    PageHelper.startPage(DEFAULT_PAGENUM,COMMENT_PAGESIZE);
//                     comments = commentService.CommentByArticleId(articleId); //一定是要mybatis查询才行，get方法获取不行
//                    redisTemplate.opsForValue().set("cm_aid_" + articleId + "_n_" + DEFAULT_PAGENUM + "_s_" + COMMENT_PAGESIZE,comments);
//                }
                PageHelper.startPage(DEFAULT_PAGENUM, COMMENT_PAGESIZE);
                List<Comment> comments = commentService.CommentByArticleId(articleId); //一定是要mybatis查询才行，get方法获取不行
                PageInfo pageInfo = new PageInfo(comments);
                modelAndView.addObject("pageInfo", pageInfo);
                modelAndView.addObject("comments", comments);
            }

            //友链
            List<link> links = linkService.selectAllLink();
            modelAndView.addObject("links", links);

            //推荐文章
            PageHelper.startPage(1,5);
            List<Article> recommends = articleService.selectArticleByRecommend();
            modelAndView.addObject("recommends",recommends);

            modelAndView.addObject("article", article);
            modelAndView.addObject("commons", Commons.getInstance());
            modelAndView.addObject("articleId", articleId);

            //跳转不同页面主题判断
            if (themeConstant.curTheme.equals(themeConstant.CALM_THEME)) { //calm主题
                modelAndView.setViewName("client/articleDetails2"); //跳转页面
                List<tag> tags = tagService.selectTags_limit8();
                modelAndView.addObject("tags", tags);
                List<Article> as = (List<Article>) redisTemplate.opsForValue().get("articleOrders10");
                if (as == null) {
                    List<Article> articleOrders = ArticleOrder_10(articleService.selectAllArticleOrderByDesc());
                    redisTemplate.opsForValue().set("articleOrders10", articleOrders, 60 * 1, TimeUnit.SECONDS);
                    modelAndView.addObject("articleOrders", articleOrders);
                } else {
                    modelAndView.addObject("articleOrders", as);
                }

            } else if (themeConstant.curTheme.equals(themeConstant.DEFAULT_THEME)) { //默认主题
                modelAndView.setViewName("client/articleDetails"); //跳转页面

            }

            return modelAndView;

        }
    }

    @ResponseBody
    @PostMapping(path = "/publishComment")
    public ArticleResponseData publishComment(Integer aid, String text, HttpSession session, HttpServletRequest request) {
        try {
            //发布评论
            Comment comment = new Comment();
            comment.setArticleId(aid);
            comment.setC_content(text);
            comment.setStatus("no_audit"); //默认未审核
            String ipAddr = ipUtils.getIpAddr(request); //获取ip
            comment.setIp(ipAddr);
            Date date = new Date(new java.util.Date().getTime());
            comment.setCreated(date);
            String username = securityUtil.currentUser(session);
            comment.setAuthor(username); //这里的用户名最好加唯一索引
            commentService.publishComment(comment);

            return ArticleResponseData.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ArticleResponseData.fail();
        }

    }


    //例如:秒杀功能 、用户信息功能


}
