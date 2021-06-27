package com.boot.controller.pearAdmin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.boot.annotation.Visitor;
import com.boot.controller.adminController;
import com.boot.data.ResponseData.layuiData;
import com.boot.data.ResponseData.layuiJSON;
import com.boot.pojo.*;
import com.boot.service.*;
import com.boot.utils.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api("新版后台系统控制器")
@Controller
@RequestMapping(path = "/pear")
@CrossOrigin
public class pearController {

    private final String DEFAULT_CATEGORY = "默认分类";

    @Autowired
    private com.boot.service.articleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private com.boot.service.statisticService statisticService;

    //log4j
    private Logger logger = Logger.getLogger(adminController.class);

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private com.boot.service.categoryService categoryService;

    @Autowired
    private com.boot.service.tagService tagService;

    @Autowired
    private userDetailService userDetailService;

    @Autowired
    private imgService imgService;

    @Autowired
    private visitorService visitorService;

    private final int type = 1;

    @Autowired
    private settingService settingService;

    private static List<String> themes = new ArrayList<>();

    private final String ECHARTS_DAYS = "echarts_days"; //redis存储日期的key

    private final String ECHARTS_COUNTS = "echarts_counts";//redis存储对应的访问量的key

    @Autowired
    private userService userService;

    @Autowired
    private userAuthorityService userAuthorityService;

    @Autowired
    private authorityService authorityService;

    @Autowired
    private linkService linkService;

    @Autowired
    private blacklistService blacklistService;


    static {
        themes.add("default");
        themes.add("calmlog");
    }

    //初始化redis有关t_tag表的数据
    @PostConstruct
    public void initTags() {
        List<tag> tags = tagService.selectAllTag();

        for (tag tag : tags) {
            redisTemplate.opsForValue().set("tag_" + tag.getTagName(), tag.getTagCount());
        }

    }


    private void charts(Model model) {
        //从缓存中查有没有近7天的缓存


        Object var1 = redisTemplate.opsForValue().get(ECHARTS_DAYS);
        Object var2 = redisTemplate.opsForValue().get(ECHARTS_COUNTS);

        List<String> ds = JSON.parseArray((String) var1, String.class);
        List<Integer> cs = JSON.parseArray((String) var2, Integer.class);
        if (ds == null || ds.size() < 7 || cs == null || cs.size() < 7) { //这种情况就要重新查
            List<String> days = visitorService.selectDaysBy7(); //维护日期
            List<Integer> counts = new ArrayList<>(); //维护访问量
            for (String day : days) {
                int count = visitorService.selectOneDayVisitor(day);
                counts.add(count);
            }

            model.addAttribute("days", days);
            model.addAttribute("counts", counts);
            //让集合变成json放入redis
            String d = JSON.toJSONString(days);
            String c = JSON.toJSONString(counts);
            //让缓存在晚上12点整点就失效
            Long second = timeUtil.getSecondByCurTimeTo12Point();
            redisTemplate.opsForValue().set(ECHARTS_DAYS, d, second, TimeUnit.SECONDS);
            redisTemplate.opsForValue().set(ECHARTS_COUNTS, c, second, TimeUnit.SECONDS);
        } else {
            //如果还有缓存（说明没有过一天），并且数据没被篡改过（都是7个数据），就执行这里的代码
            //这个时候我们只需要更新一下最后一天（也就是今天）的数据即可
            String s = ds.get(6); //获取今天的日期
            int i = visitorService.selectOneDayVisitor(s);
            cs.set(6, i);
            String list = JSON.toJSONString(cs); ////记得转换成json
            model.addAttribute("days", ds);
            model.addAttribute("counts", cs);
            //重新放入redis
            Long second = timeUtil.getSecondByCurTimeTo12Point();
            redisTemplate.opsForValue().set(ECHARTS_COUNTS, list, second, TimeUnit.SECONDS);

        }
    }


    @RequestMapping(path = "/welcome")
    public String towelcome() {

        return "back/newback/article/welcome";
    }

    //控制后台，非常重要，访问后台时，会内嵌这个url
    @RequestMapping(path = "/toconsole")
    public String toconsole(Model model, HttpSession session, HttpServletRequest request) {


        int usercount = userService.userCount(); //用户总数
        model.addAttribute("usercount", usercount);

        String username = springSecurityUtil.currentUser(session);
        setting setting = settingService.selectUserSetting(username);
        model.addAttribute("setting", setting); //传入系统设置

        String ipAddr = ipUtils.getIpAddr(request);
        logger.debug("ip:" + ipAddr + "访问了后台管理界面");

        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        logger.debug(time + "   用户名：" + username + "进入admim后台");
        PageHelper.startPage(1, 5);
        List<Article> articles = articleService.selectArticleOrderCreateDate();
        model.addAttribute("articles", articles);
        PageInfo articlePageInfo = new PageInfo(articles);
        model.addAttribute("articlePageInfo", articlePageInfo);

        List<Article> articles1 = articleService.selectArticleByRecommend();
        model.addAttribute("recommendCount", articles1.size());


        //统计图表
        charts(model);


        model.addAttribute("commons", Commons.getInstance());
        int count = articleService.selectArticleCount();
        model.addAttribute("articleCount", count);
        int i = imgService.selectImgCount();
        model.addAttribute("imgcount", i);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);

        return "back/newback/article/console1";
    }

    //登录
    @RequestMapping(path = "/login")
    public String tologin() {


        return "back/newback/login";
    }

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
     * @param articleid
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/enableComment")
    public String enableComment(Integer articleid){
        System.out.println("articleid:"+articleid+"==enableComment");

        layuiJSON json=new layuiJSON();
        json.setSuccess(true);
        json.setMsg("开启评论成功");
        return JSON.toJSONString(json);
    }

    /**
     * 关闭评论
     * @param articleid
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/disableComment")
    public String disableComment(Integer articleid){
        System.out.println("articleid:"+articleid+"==disableComment");

        layuiJSON json=new layuiJSON();
        json.setSuccess(true);
        json.setMsg("关闭评论成功");
        return JSON.toJSONString(json);
    }


    /**
     * 开启推荐
     * @param articleid
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/enableRecommend")
    public String enableRecommend(Integer articleid){
        System.out.println("articleid:"+articleid+"==enableRecommend");

        layuiJSON json=new layuiJSON();
        json.setSuccess(true);
        json.setMsg("开启推荐成功");
        return JSON.toJSONString(json);
    }

    /**
     * 取消推荐
     * @param articleid
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/disableRecommend")
    public String disableRecommend(Integer articleid){
        System.out.println("articleid:"+articleid+"==disableRecommend");

        layuiJSON json=new layuiJSON();
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


    //分类管理
    @Visitor(desc = "分类管理")
    @RequestMapping(path = "/toCategory")
    public String toCategory() {


        return "back/newback/article/categories";
    }

    @ResponseBody
    @RequestMapping(path = "/categoryData")
    public String categoryData(@RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "limit", defaultValue = "3") int limit){

        PageHelper.startPage(page,limit);
        List<category> categories = categoryService.selectCategories();

        int count=categoryService.selectCategoryCount();
        layuiData<category> layuiData=new layuiData<>();
        layuiData.setCode(0);
        layuiData.setMsg("");
        layuiData.setCount(count);
        layuiData.setData(categories);

        return JSON.toJSONString(layuiData);

    }


    //标签管理
    @Visitor(desc = "标签管理")
    @RequestMapping(path = "/toTag")
    public String toTag() {


        return "back/newback/article/tag_list";
    }

    @RequestMapping(path = "/tagsData")
    @ResponseBody
    public String tagsData(@RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "limit", defaultValue = "3") int limit) {


        layuiData<tag> taglayuiData = new layuiData<>();

        PageHelper.startPage(page,limit);
        List<tag> tags = tagService.selectAllTag();

        int count = tagService.selectTagCount();
        taglayuiData.setCode(0);
        taglayuiData.setMsg("");
        taglayuiData.setData(tags);
        taglayuiData.setCount(count);

        return JSON.toJSONString(taglayuiData);
    }



    //附件管理
    @Visitor(desc = "附件管理")
    @RequestMapping(path = "/toFileUpload")
    public String toFileUpload(Model model) {

        List<img> imgs = imgService.selectAllImg();
        model.addAttribute("imgs",imgs);



        return "back/newback/article/img_list";
    }

    //爬取数据
    @Visitor(desc = "爬取数据")
    @RequestMapping(path = "/toCatchData")
    public String toCatchData() {


        return "back/newback/article/catch_list";
    }

    //用户管理
    @Visitor(desc = "用户管理")
    @RequestMapping(path = "/toUserManager")
    public String toUserManager() {


        return "back/newback/article/userManager";
    }

    @ResponseBody
    @RequestMapping(path = "/userManagerData")
    public String userManagerData(@RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "limit", defaultValue = "6") int limit,
                                  HttpSession session){

        layuiData<user> json=new layuiData<>();

        PageHelper.startPage(page,limit);
        List<user> users = userService.selectAllUser();
        String name = springSecurityUtil.currentUser(session);

        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        logger.debug(time + "   用户名：" + name + "进入后台用户管理页面");

        int total = userService.userCount();

        json.setCode(0);
        json.setCount(total); //总数
        json.setMsg("");
        json.setData(users); //分页数据

        return JSON.toJSONString(json);
    }


    //友链管理
    @Visitor(desc = "友链管理")
    @RequestMapping(path = "/toLink")
    public String toLink() {


        return "back/newback/article/link_list";
    }

    /**
     *
     * @param page layui分页默认会传page和limit的值，所以要进行接收
     * @param limit
     * @return json
     */
    @ResponseBody
    @RequestMapping(path = "/linkData")
    public String linkData(@RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "limit", defaultValue = "6") int limit){


        layuiData<link> linklayuiData = new layuiData<>();
        PageHelper.startPage(page,limit);
        List<link> links = linkService.selectAllLink();
        int count = linkService.linkCount();
        linklayuiData.setCode(0);
        linklayuiData.setMsg("");
        linklayuiData.setCount(count);
        linklayuiData.setData(links);


        return JSON.toJSONString(linklayuiData);
    }




    //个人资料
    @Visitor(desc = "个人资料")
    @RequestMapping(path = "/touser")
    public String touser(HttpSession session,Model model) {

        String name = springSecurityUtil.currentUser(session);

        user user = userService.selectUserInfoByuserName(name);


        model.addAttribute("user",user);
        model.addAttribute("curName",name);
        model.addAttribute("commons", Commons.getInstance());
        model.addAttribute("bootstrap",new bootstrap());

        userDetail userDetail = userDetailService.selectUserDetailByUserName(name);
        model.addAttribute("userDetail",userDetail);

        return "back/newback/article/user_list";
    }

    @ResponseBody
    @RequestMapping(path = "/userInfo")
    public String userInfo(userDetail userDetail,String email, MultipartFile file){
        System.out.println("============="+userDetail.toString()+email+"==file"+file);
        layuiJSON layuiJSON = new layuiJSON();
        layuiJSON.setMsg("666");
//        Map map = JSONObject.parseObject(jsonData, Map.class); //把前端传来的json转换成Map
//        System.out.println(map);
        layuiJSON.setSuccess(true);

        return JSON.toJSONString(layuiJSON);
    }

    @ResponseBody
    @RequestMapping(path = "/updatePassword")
    public String updatePassword(password password){

        System.out.println(password);

        layuiJSON layuiJSON = new layuiJSON();
        layuiJSON.setMsg("666");
        if(password.getNewPassword().equals(password.getCheckPassword())){
            layuiJSON.setSuccess(true);
        }else {
            layuiJSON.setSuccess(false);
        }
        return JSON.toJSONString(layuiJSON);
    }




    //访客记录
    @Visitor(desc = "访客记录")
    @RequestMapping(path = "/toVisitor")
    public String toVisitor() {


        return "back/newback/article/visitor_list";
    }

    @ResponseBody
    @RequestMapping(path = "/visitorData")
    public String visitorData(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "limit", defaultValue = "10") int limit){


        layuiData<visitor> visitorlayuiData = new layuiData<>();

        PageHelper.startPage(page,limit);
        List<visitor> visitors = visitorService.selectVisitor();

        int count = visitorService.selectVistorCount();
        visitorlayuiData.setCode(0);
        visitorlayuiData.setCount(count);
        visitorlayuiData.setMsg("");
        visitorlayuiData.setData(visitors);

        return JSON.toJSONString(visitorlayuiData);
    }



    //黑名单
    @Visitor(desc = "黑名单")
    @RequestMapping(path = "/toBlack")
    public String toBlack() {


        return "back/newback/article/black_list";
    }

    @ResponseBody
    @RequestMapping(path = "/blackData")
    public String blackData(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "limit", defaultValue = "10")int limit){

        layuiData<blacklist> blacklistlayuiData = new layuiData<>();

        PageHelper.startPage(page,limit);
        List<blacklist> blacklists = blacklistService.selectBlackList();

        int count = blacklistService.selectBlackCount();

        blacklistlayuiData.setCode(0);
        blacklistlayuiData.setMsg("");
        blacklistlayuiData.setData(blacklists);
        blacklistlayuiData.setCount(count);

        return JSON.toJSONString(blacklistlayuiData);
    }

    //拦截记录
    @RequestMapping(path = "/toInterceptorLog")
    public String toInterceptorLog() {


        return "back/newback/article/interceptor_list";
    }

    //行为日志
    @RequestMapping(path = "/toLog")
    public String toLog() {


        return "back/newback/article/log_list";
    }

    //数据监控
    @RequestMapping(path = "/toMonitor")
    public String toMonitor() {


        return "back/newback/article/monitor";
    }

    //数据图表
    @RequestMapping(path = "/toEcharts")
    public String toEcharts(Model model,HttpSession session) {

        //提供文章阅读量数据
        List<Article> articles = articleService.selectArticleStatistic();
        model.addAttribute("articles", articles);

        //提供分类数的数据
        List<category> categories = categoryService.selectCategories_echarts();
        model.addAttribute("categories", categories);
        //提供标签数的数据
        List<tag> tags = tagService.selectTags_echarts();
//        tags.get(0).getTagName()
//        tags.get(0).getTagCount()
        model.addAttribute("tags", tags);

        //从缓存中查有没有近7天的缓存

        Object var1 =redisTemplate.opsForValue().get(ECHARTS_DAYS);
        Object var2 =redisTemplate.opsForValue().get(ECHARTS_COUNTS);

        List<String> ds = JSON.parseArray((String) var1, String.class);
        List<Integer> cs = JSON.parseArray((String) var2, Integer.class);

        if (ds == null || ds.size() < 7 || cs == null || cs.size() < 7) { //这种情况就要重新查
            List<String> days = visitorService.selectDaysBy7(); //维护日期
            List<Integer> counts = new ArrayList<>(); //维护访问量
            for (String day : days) {
                int count = visitorService.selectOneDayVisitor(day);
                counts.add(count);
            }
            model.addAttribute("days", days);
            model.addAttribute("counts", counts);
            //让集合变成json放入redis
            String d = JSON.toJSONString(days);
            String c = JSON.toJSONString(counts);
            //让缓存在晚上12点整点就失效
            Long second = timeUtil.getSecondByCurTimeTo12Point();
            redisTemplate.opsForValue().set(ECHARTS_DAYS, d, second, TimeUnit.SECONDS);
            redisTemplate.opsForValue().set(ECHARTS_COUNTS, c, second, TimeUnit.SECONDS);
        } else {
            //如果还有缓存（说明没有过一天），并且数据没被篡改过（都是7个数据），就执行这里的代码
            //这个时候我们只需要更新一下最后一天（也就是今天）的数据即可
            String s = ds.get(6); //获取今天的日期
            int i = visitorService.selectOneDayVisitor(s);
            cs.set(6,i);
            String list = JSON.toJSONString(cs); ////记得转换成json
            model.addAttribute("days", ds);
            model.addAttribute("counts", cs);
            //重新放入redis
            Long second = timeUtil.getSecondByCurTimeTo12Point();
            redisTemplate.opsForValue().set(ECHARTS_COUNTS, list, second, TimeUnit.SECONDS);

        }

        String username = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);
        model.addAttribute("commons", Commons.getInstance());



        return "back/newback/article/statistic_charts";
    }

    //系统设置
    @RequestMapping(path = "/toSetting")
    public String toSetting() {


        return "back/newback/article/setting";
    }


}
