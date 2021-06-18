package com.boot.controller;

import com.boot.annotation.Visitor;
import com.boot.data.ResponseData.ArticleResponseData;
import com.boot.pojo.*;
import com.boot.service.*;
import com.boot.utils.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 游政杰
 * 2021/5/20 17:08
 */
@Controller
@RequestMapping("/admin")
@Api(value = "后台管理控制器")
public class adminController {

    private final String DEFAULT_CATEGORY = "默认分类";

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

    @Autowired
    private imgService imgService;

    @Autowired
    private visitorService visitorService;

    private final int type = 1;


    //初始化redis有关t_tag表的数据
    @PostConstruct
    public void initTags() {
        List<tag> tags = tagService.selectAllTag();

        for (tag tag : tags) {
            redisTemplate.opsForValue().set("tag_" + tag.getTagName(), tag.getTagCount());
        }

    }


    @Visitor(desc = "进入后台界面")
    @GetMapping(path = "/")
    @ApiOperation(value = "去后台管理界面", notes = "以/作为路径进入")
    public String toAdmin(Model model, HttpSession session, HttpServletRequest request) {



        String ipAddr = ipUtils.getIpAddr(request);
        logger.debug("ip:" + ipAddr + "访问了后台管理界面");

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
        int i = imgService.selectImgCount();
        model.addAttribute("imgcount", i);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);

        return "back/index";
    }

    @Visitor(desc = "进入发布文章界面")
    @RequestMapping(path = "/toPublishArticle")
    @ApiOperation(value = "进入发布文章界面", notes = "进入发布文章界面")
    public String toPublishArticle(Model model, HttpSession session, HttpServletRequest request) {



        String username = springSecurityUtil.currentUser(session);
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        String ipAddr = ipUtils.getIpAddr(request);
        logger.debug(time + "   用户名：" + username + "进入后台发布页面：ip为" + ipAddr);
        model.addAttribute("commons", Commons.getInstance());

        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);
        return "back/article_edit";
    }


    // 默认第一页
    @Visitor(desc = "进入文章列表界面")
    @RequestMapping(path = "/toArticleList")
    @ApiOperation(value = "进入文章列表界面", notes = "进入文章列表界面，分页默认是第一页")
    public String toArticleList1(Model model, HttpSession session, HttpServletRequest request) {


        String username = springSecurityUtil.currentUser(session);
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        String ipAddr = ipUtils.getIpAddr(request);
        logger.debug(time + "   用户名：" + username + "查看文章列表,ip为：" + ipAddr);
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
        model.addAttribute("userDetail", userDetail);
        return "back/article_list";
    }


    @Visitor(desc = "进入文章列表界面")
    @RequestMapping(path = "/toArticleList/{pageNum}")
    @ApiOperation(value = "进入文章列表界面", notes = "进入文章列表界面,分页是由前端传入")
    public String toArticleList2(@PathVariable("pageNum") Integer pageNum, Model model, HttpSession session, HttpServletRequest request) {



        String username = springSecurityUtil.currentUser(session);
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        String ipAddr = ipUtils.getIpAddr(request);
        logger.debug(time + "   用户名：" + username + "查看文章列表,ip为：" + ipAddr);
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
        model.addAttribute("userDetail", userDetail);
        return "back/article_list";
    }

    @Visitor(desc = "进入编辑文章页面")
    @RequestMapping(path = "/toEditArticle/{article_id}")
    @ApiOperation(value = "进入编辑文章页面")
    public String toEditArticle(@PathVariable("article_id") Integer article_id, HttpSession session, Model model, HttpServletRequest request) {


        String username = springSecurityUtil.currentUser(session);
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        logger.debug(time + "   用户名：" + username + "进入文章编辑页面");
        model.addAttribute("commons", Commons.getInstance());
        Article article = articleService.selectArticleByArticleIdNoComment(article_id);
        model.addAttribute("contents", article);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);
        return "back/article_edit";
    }


    @RequestMapping(path = "/modify")
    @ResponseBody //要加
    @ApiOperation(value = "修改文章")
    public ArticleResponseData modify(Article article, Model model, HttpSession session, HttpServletRequest request) {
        model.addAttribute("commons", Commons.getInstance());
//        System.out.println("modify:" + article);
        List<Article> articles = articleService.selectAllArticleByCreated();
        model.addAttribute("articles", articles);

        try {
            articleService.changeArticle_service(article);//进行修改文章

            //打印修改成功日志
            String username = springSecurityUtil.currentUser(session);
            java.util.Date date2 = new java.util.Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time2 = simpleDateFormat.format(date2);
            String ipAddr = ipUtils.getIpAddr(request);
            logger.debug(time2 + "   用户名：" + username + "修改文章信息成功,ip为：" + ipAddr);
            return ArticleResponseData.ok();
        } catch (Exception e) {
            /**
             * 解决方案：为了解决因为spring事务只会对mysql进行回滚而不会对redis操作进行回滚
             * 所以我们可以把redis数据进行重新导入
             */
            List<tag> tags = tagService.selectAllTag();

            for (tag tag : tags) {
                redisTemplate.opsForValue().set("tag_" + tag.getTagName(), tag.getTagCount());
            }
            e.printStackTrace();
            return ArticleResponseData.fail();
        }


    }


    @RequestMapping(path = "/publish")
    @ResponseBody //要加
    @ApiOperation("发布文章")
    public ArticleResponseData publish(Article article, HttpSession session, Model model, HttpServletRequest request) {
        model.addAttribute("commons", Commons.getInstance());
//        System.out.println("publish:" + article);
        List<Article> articles = articleService.selectAllArticleByCreated();
        model.addAttribute("articles", articles);
        try {
            //发布操作代码
            articleService.publishArticle_service(article);

            //打印日志操作
            String username = springSecurityUtil.currentUser(session);
            java.util.Date date2 = new java.util.Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(date2);
            String ipAddr = ipUtils.getIpAddr(request);
            logger.debug(time + "   用户名：" + username + "发布成功,ip为：" + ipAddr);
//            redisTemplate.delete("articleList");
            return ArticleResponseData.ok();
        } catch (Exception e) {
            /**
             * 在publishArticle_service方法中，因为操作数据库的代码都在操作redis的上面
             * 所以当操作数据库的代码报错，会立刻进行回滚，所以我们大可不用担心数据库的错误
             * redis的语句如果报错则也会触发数据库的回滚，并且redis也执行不成功
             * 所以我们不用在controller层进行数据的恢复。
             */
            e.printStackTrace();
            return ArticleResponseData.fail();
        }
    }


    @RequestMapping(path = "/deleteArticle")
    @ResponseBody
    @ApiOperation("删除文章")
    public ArticleResponseData deleteArticle(Integer id, HttpSession session, HttpServletRequest request) {

        try {
            //删除文章
            articleService.deleteArticle_service(id);

            String username = springSecurityUtil.currentUser(session);
            java.util.Date date = new java.util.Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(date);
            String ipAddr = ipUtils.getIpAddr(request);
            logger.debug(time + "   用户名：" + username + "删除文章成功，删除的文章id为：" + id + ",ip为：" + ipAddr);
//            redisTemplate.delete("articleList");
            return ArticleResponseData.ok();
        } catch (Exception e) {
            /**
             * deleteArticle_service方法for (String s : split) {}
             * 当循环次数大于1次时，如果发生了错误，一定会操作数据库和redis数据不一致的情况
             * 因为redis不会进行回滚，但是redis却把已有标签数-1，所以我们还是用同一套解决方法
             * 把数据库的tag标签表再一次导入到redis中
             */
            List<tag> tags = tagService.selectAllTag();

            for (tag tag : tags) {
                redisTemplate.opsForValue().set("tag_" + tag.getTagName(), tag.getTagCount());
            }
            e.printStackTrace();
            return ArticleResponseData.fail();
        }
    }

    //    评论管理
    //跳转评论列表兼处理审核通过请求、删除请求
    @Visitor(desc = "进入评论列表")
    @RequestMapping(path = "/toCommentList")
    @ApiOperation("进入评论列表")
    public String toCommentList(@RequestParam(value = "delId", defaultValue = "-1") int delId,
                                @RequestParam(value = "id", defaultValue = "-1") int id,
                                @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                HttpSession session, HttpServletRequest request,
                                Model model) {



        String username = springSecurityUtil.currentUser(session);
        //处理审核
        if (id != -1) {
            try {
                commentService.checkSuccess(id);
            } catch (Exception e) {
                logger.error("修改评论失败。。id=" + id);
                e.printStackTrace();
            }
        }

        //处理删除请求
        if (delId != -1) {
            //可以删除
            try {
                commentService.deleteComment(delId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        model.addAttribute("commons", Commons.getInstance());
        PageHelper.startPage(pageNum, 5);
        List<Comment> comments = commentService.selectCommentsOrderCreateDate();
        PageInfo pageInfo = new PageInfo(comments);
        model.addAttribute("comments", comments);
        model.addAttribute("pageInfo", pageInfo);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);
        return "back/comment_list";
    }


    @Visitor(desc = "进入分类标签界面")
    @RequestMapping(path = "/toTagList")
    @ApiOperation("进入分类标签界面")
    public String toTagList(HttpSession session, Model model, HttpServletRequest request) {

        String username = springSecurityUtil.currentUser(session);

        List<category> categories = categoryService.selectCategories();
        List<tag> tags = tagService.selectAllTag();

        model.addAttribute("tags", tags);
        model.addAttribute("categories", categories);
        model.addAttribute("bootstrap", new bootstrap());
        model.addAttribute("commons", Commons.getInstance());
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);
        return "back/categories";
    }


    //修改分类
    @PostMapping(path = "/updateCategory")
    @ApiOperation("修改分类")
    public String updateCategory(String oldName, String newName, HttpSession session, Model model) {
        try {

            categoryService.updateCategory_service(oldName, newName);

        } catch (Exception e) {
            e.printStackTrace();
        }

        List<category> categories = categoryService.selectCategories();
        List<tag> tags = tagService.selectAllTag();

        model.addAttribute("tags", tags);
        model.addAttribute("categories", categories);
        model.addAttribute("bootstrap", new bootstrap());
        model.addAttribute("commons", Commons.getInstance());

        String username = springSecurityUtil.currentUser(session);

        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);

        return "back/categories";

    }

    @RequestMapping(path = "/deleteCategory")
    @ApiOperation("删除分类")
    public String deleteCategory(@RequestParam(value = "n", defaultValue = "") String n, HttpSession session, Model model) {
        if (n != null && !n.equals("")) {
            try {
                categoryService.deleteCategory_service(n, DEFAULT_CATEGORY);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        List<category> categories = categoryService.selectCategories();
        List<tag> tags = tagService.selectAllTag();

        model.addAttribute("tags", tags);
        model.addAttribute("categories", categories);
        model.addAttribute("bootstrap", new bootstrap());
        model.addAttribute("commons", Commons.getInstance());

        String username = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);

        return "back/categories";
    }


    @PostMapping(path = "/addCategory")
    @ApiOperation("添加分类")
    public String addCategory(category category, HttpSession session, Model model) {

        try {
            categoryService.addCategory(category);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<category> categories = categoryService.selectCategories();
        List<tag> tags = tagService.selectAllTag();

        model.addAttribute("tags", tags);
        model.addAttribute("categories", categories);
        model.addAttribute("bootstrap", new bootstrap());
        model.addAttribute("commons", Commons.getInstance());

        String username = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);

        return "back/categories";
    }


    @Visitor(desc = "进入系统设置界面")
    @RequestMapping(path = "/toSetting")
    @ApiOperation("进入系统设置界面")
    public String toSetting(HttpSession session, Model model, HttpServletRequest request) {


        model.addAttribute("commons", Commons.getInstance());
        model.addAttribute("bootstrap", new bootstrap());

        String username = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);

        return "back/setting";
    }


    //秒杀产品服务


}