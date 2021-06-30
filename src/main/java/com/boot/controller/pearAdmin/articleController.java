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
import com.boot.utils.bootstrap;
import com.boot.utils.ipUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
/**
 * @author 游政杰
 * @Date 2021/6/30
 */
@Controller(value = "pearArticleController")
@RequestMapping(path = "/pear")
@CrossOrigin
public class articleController {

    private final String DEFAULT_CATEGORY = "默认分类";

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

    @Autowired
    private RedisTemplate redisTemplate;

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

        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);
        model.addAttribute("ps","发布文章");
        model.addAttribute("url","/article/publish");
        return "back/newback/article/article_edit";
    }


    @RequestMapping(path = "/article/publish")
    @ResponseBody
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


    //修改文章
    @RequestMapping(path = "/toChangeArticle")
    public String toChangeArticle(int article_id,Model model, HttpSession session, HttpServletRequest request) {
        System.out.println(article_id);
        String username = springSecurityUtil.currentUser(session);
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        logger.debug(time + "   用户名：" + username + "进入文章编辑页面");
        Article article = articleService.selectArticleByArticleIdNoComment(article_id);
        model.addAttribute("contents", article);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);
        model.addAttribute("ps","修改文章");
        model.addAttribute("url","/modifyArticle");
        return "back/newback/article/article_edit";
    }



    @RequestMapping(path = "/modifyArticle")
    @ResponseBody //要加
    @ApiOperation(value = "修改文章")
    public String modify(String content,@RequestParam(value = "editArticleId",defaultValue = "-99") int editArticleId,
                                      Article article, HttpSession session,
                                      HttpServletRequest request) {
        layuiJSON json=new layuiJSON();

         if(editArticleId==-99){ //=-99说明不能修改
             json.setSuccess(false);
             json.setMsg("修改失败");
             return JSON.toJSONString(json);
         }else { //可修改
             try {
                 article.setId(editArticleId);
                 articleService.changeArticle_service(article);//进行修改文章
                 //打印修改成功日志
                 String username = springSecurityUtil.currentUser(session);
                 java.util.Date date2 = new java.util.Date();
                 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 String time2 = simpleDateFormat.format(date2);
                 String ipAddr = ipUtils.getIpAddr(request);
                 logger.debug(time2 + "   用户名：" + username + "修改文章信息成功,ip为：" + ipAddr);
                 json.setSuccess(true);
                 json.setMsg("修改成功");
                 return JSON.toJSONString(json);
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
                 json.setSuccess(false);
                 json.setMsg("修改失败");
                 return JSON.toJSONString(json);
             }

         }

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
    public String deleteArticle(@PathVariable("articleid") int articleid,HttpSession session
                                ,HttpServletRequest request) {

        layuiJSON layuiArticleJSON = new layuiJSON();


        try {
            //删除文章
            articleService.deleteArticle_service(articleid);

            String username = springSecurityUtil.currentUser(session);
            java.util.Date date = new java.util.Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(date);
            String ipAddr = ipUtils.getIpAddr(request);
            logger.debug(time + "   用户名：" + username + "删除文章成功，删除的文章id为：" + articleid + ",ip为：" + ipAddr);
//            redisTemplate.delete("articleList");
            layuiArticleJSON.setMsg("删除文章成功");
            layuiArticleJSON.setSuccess(true);
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
            layuiArticleJSON.setMsg("删除文章失败");
            layuiArticleJSON.setSuccess(false);
        }

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
        layuiJSON json = new layuiJSON();
        try {
            articleService.updateRecommendTo_1(articleid);

            json.setMsg("开启推荐成功");
            json.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            json.setMsg("开启推荐失败");
            json.setSuccess(false);
        }

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


        layuiJSON json = new layuiJSON();

        try {
            articleService.updateRecommendTo_0(articleid);
            json.setSuccess(true);
            json.setMsg("取消推荐成功");
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("取消推荐失败");
        }

        return JSON.toJSONString(json);
    }

    @ResponseBody
    @RequestMapping(path = "/batchRemove/{checkIds}")
    public String batchRemoveArticle(@PathVariable("checkIds") String checkIds) {

        layuiJSON layuiArticleJSON = new layuiJSON();

        try {

            String[] var = checkIds.split(",");
            for (String v : var) {
                articleService.deleteArticle_service(Integer.parseInt(v));
            }
            layuiArticleJSON.setSuccess(true);
            layuiArticleJSON.setMsg("批量删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            layuiArticleJSON.setSuccess(false);
            layuiArticleJSON.setMsg("批量删除失败");
        }

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

    @RequestMapping(path = "/addCategoryPage")
    public String addCategoryPage(){

        return "back/newback/article/module/addCategory";
    }


    @PostMapping(path = "/add/Category")
    @ResponseBody
    @ApiOperation("添加分类")
    public String addCategory(category category, HttpSession session, Model model) {
        layuiJSON json=new layuiJSON();

        try {
            categoryService.addCategory(category);
            json.setSuccess(true);
            json.setMsg("添加分类成功");
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("添加分类失败");
        }
        return JSON.toJSONString(json);

    }

    @RequestMapping(path = "/modifyCategoryPage")
    public String modifyCategoryPage(Model model,String oldName){
        model.addAttribute("oldName",oldName);
        return "back/newback/article/module/modifyCategory";
    }

    //修改分类
    @PostMapping(path = "/modify/Category")
    @ResponseBody
    @ApiOperation("修改分类")
    public String modifyCategory(String oldName,String newName,HttpSession session, Model model) {
        layuiJSON json=new layuiJSON();

        try {
            categoryService.updateCategory(oldName, newName);
            json.setSuccess(true);
            json.setMsg("修改分类成功");
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("修改分类失败");
        }
        return JSON.toJSONString(json);
    }


    @RequestMapping(path = "/delete/Category")
    @ResponseBody
    @ApiOperation("删除分类")
    public String deleteCategory(@RequestParam(value = "n", defaultValue = "") String n, HttpSession session, Model model) {

        layuiJSON json=new layuiJSON();

        if (n != null && !n.equals("")) {
            try {
                categoryService.deleteCategory_service(n, DEFAULT_CATEGORY);
                json.setMsg("删除分类成功");
                json.setSuccess(true);
            } catch (Exception e) {
                e.printStackTrace();
                json.setMsg("删除分类失败");
                json.setSuccess(false);
            }

        }else {
            json.setMsg("删除分类失败");
            json.setSuccess(false);
        }

        return JSON.toJSONString(json);
    }



    @RequestMapping(path = "/batchRemove/Category")
    @ResponseBody
    @ApiOperation("批量删除分类")
    /**
     * @Param checkIds: 分类名的集合
     */
    public String batchRemoveCategory(@RequestParam(value = "checkIds", defaultValue = "") String checkIds, HttpSession session, Model model) {

        layuiJSON json=new layuiJSON();

        try {
            String[] split = checkIds.split(",");
            for (String s : split) {
                categoryService.deleteCategory_service(s, DEFAULT_CATEGORY);
            }
            json.setMsg("批量删除分类成功");
            json.setSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
            json.setMsg("批量删除分类失败");
            json.setSuccess(false);
        }

        return JSON.toJSONString(json);
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
