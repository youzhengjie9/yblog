package com.boot.controller.pearAdmin;

import com.alibaba.fastjson.JSON;
import com.boot.annotation.Visitor;
import com.boot.data.ResponseData.ArticleResponseData;
import com.boot.data.ResponseData.layuiData;
import com.boot.data.ResponseData.layuiJSON;
import com.boot.pojo.Article;
import com.boot.pojo.category;
import com.boot.pojo.tag;
import com.boot.pojo.userDetail;
import com.boot.service.articleService;
import com.boot.service.categoryService;
import com.boot.service.tagService;
import com.boot.service.userDetailService;
import com.boot.utils.Commons;
import com.boot.utils.SpringSecurityUtil;
import com.boot.utils.ipUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller(value = "pearArticleController")
@RequestMapping(path = "/pear")
@CrossOrigin
public class articleController {

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    private Logger logger = Logger.getLogger(articleController.class);

    @Autowired
    private userDetailService userDetailService;

    @Autowired
    private articleService articleService;

    @Autowired
    private categoryService categoryService;

    @Autowired
    private tagService tagService;

    //文章
    //发布文章
    @RequestMapping(path = "/topublish")
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


        return "back/newback/article/article_edit";
    }


    @RequestMapping(path = "/article/publish")
    @ResponseBody
    @ApiOperation("发布文章")
    public String publish(Article article, HttpSession session, HttpServletRequest request) {

        layuiJSON json = new layuiJSON(); //封装json数据传入前台

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
            json.setSuccess(true);
            json.setMsg("发布成功");
        } catch (Exception e) {
            /**
             * 在publishArticle_service方法中，因为操作数据库的代码都在操作redis的上面
             * 所以当操作数据库的代码报错，会立刻进行回滚，所以我们大可不用担心数据库的错误
             * redis的语句如果报错则也会触发数据库的回滚，并且redis也执行不成功
             * 所以我们不用在controller层进行数据的恢复。
             */
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("发布失败");
        }
        return JSON.toJSONString(json);
    }


    //文章管理
    @Visitor(desc = "进入文章列表界面")
    @RequestMapping(path = "/toArticleManager")
    @ApiOperation(value = "进入文章列表界面", notes = "进入文章列表界面，分页默认是第一页")
    public String toArticleManager1(Model model, HttpSession session, HttpServletRequest request) {

        String username = springSecurityUtil.currentUser(session);
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        String ipAddr = ipUtils.getIpAddr(request);
        logger.debug(time + "   用户名：" + username + "查看文章列表,ip为：" + ipAddr);
        model.addAttribute("commons", Commons.getInstance());


        PageHelper.startPage(1, 6);
        List<Article> articles = articleService.selectAllArticleByCreated();
        PageInfo pageInfo = new PageInfo(articles);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("articles", articles);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);


        return "back/newback/article/article_list";
    }

    @ResponseBody
    @RequestMapping("/articledata")
    public String articleData(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "limit", defaultValue = "6") int limit) {

        int total = articleService.selectArticleCount();

        PageHelper.startPage(page, limit);
        List<Article> articles = articleService.selectAllArticleByCreated();
        for (Article article : articles) {
            article.setContent(null);
        }
        System.out.println(articles);
        layuiData<Article> layuiArticleData = new layuiData<Article>();
        layuiArticleData.setCode(0);
        layuiArticleData.setMsg("");
        layuiArticleData.setCount(total); //“”总共“”的记录数
        layuiArticleData.setData(articles); //“”分页“”后的数据
        return JSON.toJSONString(layuiArticleData);
    }

    @ResponseBody
    @RequestMapping(path = "/deleteArticle/{articleid}")
    public String deleteArticle(@PathVariable("articleid") int articleid) {

        System.out.println("deleteArticle:" + articleid);
        layuiJSON layuiArticleJSON = new layuiJSON();
        if (articleid == 37) {
            layuiArticleJSON.setSuccess(true);
        } else {
            layuiArticleJSON.setSuccess(false);
        }

        layuiArticleJSON.setMsg("hello");

        return JSON.toJSONString(layuiArticleJSON);
    }

    /**
     * 开启评论
     *
     * @param articleid
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/enableComment")
    public String enableComment(Integer articleid) {
        System.out.println("articleid:" + articleid + "==enableComment");

        layuiJSON json = new layuiJSON();
        json.setSuccess(true);
        json.setMsg("开启评论成功");
        return JSON.toJSONString(json);
    }

    /**
     * 关闭评论
     *
     * @param articleid
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/disableComment")
    public String disableComment(Integer articleid) {
        System.out.println("articleid:" + articleid + "==disableComment");

        layuiJSON json = new layuiJSON();
        json.setSuccess(true);
        json.setMsg("关闭评论成功");
        return JSON.toJSONString(json);
    }

    /**
     * 开启推荐
     *
     * @param articleid
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/enableRecommend")
    public String enableRecommend(Integer articleid) {
        System.out.println("articleid:" + articleid + "==enableRecommend");

        layuiJSON json = new layuiJSON();
        json.setSuccess(true);
        json.setMsg("开启推荐成功");
        return JSON.toJSONString(json);
    }

    /**
     * 取消推荐
     *
     * @param articleid
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/disableRecommend")
    public String disableRecommend(Integer articleid) {
        System.out.println("articleid:" + articleid + "==disableRecommend");

        layuiJSON json = new layuiJSON();
        json.setSuccess(true);
        json.setMsg("取消推荐成功");
        return JSON.toJSONString(json);
    }

    @ResponseBody
    @RequestMapping(path = "/batchRemove/{checkIds}")
    public String batchRemoveArticle(@PathVariable("checkIds") String checkIds) {

        System.out.println("batchRemoveArticle:" + checkIds);
        layuiJSON layuiArticleJSON = new layuiJSON();

        layuiArticleJSON.setSuccess(true);

        layuiArticleJSON.setMsg("batch");

        return JSON.toJSONString(layuiArticleJSON);
    }

    @ResponseBody
    @RequestMapping(path = "/categoryData")
    public String categoryData(@RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "limit", defaultValue = "3") int limit) {

        PageHelper.startPage(page, limit);
        List<category> categories = categoryService.selectCategories();

        int count = categoryService.selectCategoryCount();
        layuiData<category> layuiData = new layuiData<>();
        layuiData.setCode(0);
        layuiData.setMsg("");
        layuiData.setCount(count);
        layuiData.setData(categories);

        return JSON.toJSONString(layuiData);

    }

    @RequestMapping(path = "/tagsData")
    @ResponseBody
    public String tagsData(@RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "limit", defaultValue = "3") int limit) {


        layuiData<tag> taglayuiData = new layuiData<>();

        PageHelper.startPage(page, limit);
        List<tag> tags = tagService.selectAllTag();

        int count = tagService.selectTagCount();
        taglayuiData.setCode(0);
        taglayuiData.setMsg("");
        taglayuiData.setData(tags);
        taglayuiData.setCount(count);

        return JSON.toJSONString(taglayuiData);
    }
}
