package com.boot.controller.pearAdmin;

import com.alibaba.fastjson.JSON;
import com.boot.annotation.Operation;
import com.boot.annotation.Visitor;
import com.boot.controller.AdminController;
import com.boot.data.ResponseData.LayuiData;
import com.boot.data.ResponseData.LayuiJSON;
import com.boot.pojo.*;
import com.boot.service.*;
import com.boot.utils.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 游政杰
 * @Date 2021/6/30
 */
@Api("新版后台系统控制器")
@Controller
@RequestMapping(path = "/pear")
@CrossOrigin
public class PearController {

    private final String DEFAULT_CATEGORY = "默认分类";

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private StatisticService statisticService;

    //log4j
    private Logger logger = Logger.getLogger(AdminController.class);

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private ImgService imgService;

    @Autowired
    private VisitorService visitorService;

    private final int type = 1;

    @Autowired
    private SettingService settingService;

    private static List<String> themes = new ArrayList<>();
    //redis存储日期的key
    private final String ECHARTS_DAYS = "echarts_days";
    //redis存储对应的访问量的key
    private final String ECHARTS_COUNTS = "echarts_counts";

    @Autowired
    private UserService userService;

    @Autowired
    private UserAuthorityService userAuthorityService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private BlackListService blacklistService;

    @Autowired
    private InterceptService interceptService;

    @Autowired
    private OperationService operationService;


    static {
        themes.add("default");
        themes.add("calmlog");
    }

    //初始化redis有关t_tag表的数据
    @PostConstruct
    public void initTags() {
        List<Tag> tags = tagService.selectAllTag();

        for (Tag tag : tags) {
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
            Long second = TimeUtil.getSecondByCurTimeTo12Point();
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
            Long second = TimeUtil.getSecondByCurTimeTo12Point();
            redisTemplate.opsForValue().set(ECHARTS_COUNTS, list, second, TimeUnit.SECONDS);

        }
    }


    @ResponseBody
    @RequestMapping(path = "/userInfoData")
    public String userInfoData(HttpSession session){
        LayuiJSON json=new LayuiJSON();
        HashMap<String, String> hashMap = new HashMap<>();
        String username = springSecurityUtil.currentUser(session);
        User user = userService.selectUserInfoByuserName(username);
        String icon = user.getUserDetail().getIcon();
        if (StringUtils.isEmpty(icon)){
            hashMap.put("icon","/pear-admin/images/avatar.jpg");
        }else {
            hashMap.put("icon",icon);
        }
        hashMap.put("username",username);

        json.setMsg(JSON.toJSONString(hashMap));

        return JSON.toJSONString(json);
    }


    //控制后台，非常重要，访问后台时，会内嵌这个url
    @Operation("进入控制后台界面")
    @RequestMapping(path = "/toconsole")
    public String toconsole(Model model, HttpSession session, HttpServletRequest request) {


        //最新日志
        List<OperationLog> operationLogs = operationService.selectOperationLogByLimit(8);
        model.addAttribute("operationLogs",operationLogs);

        int usercount = userService.userCount(); //用户总数
        model.addAttribute("usercount", usercount);

        String username = springSecurityUtil.currentUser(session);
        Setting setting = settingService.selectUserSetting(username);
        model.addAttribute("setting", setting); //传入系统设置

        String ipAddr = IpUtils.getIpAddr(request);
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
        UserDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);

        return "back/newback/article/console1";
    }

    //登录
    @RequestMapping(path = "/login")
    public String tologin() {


        return "back/newback/login";
    }


    //分类管理
    @Operation("进入分类管理界面")
    @Visitor(desc = "分类管理")
    @RequestMapping(path = "/toCategory")
    public String toCategory() {


        return "back/newback/article/categories";
    }

    //标签管理
    @Operation("进入标签管理界面")
    @Visitor(desc = "标签管理")
    @RequestMapping(path = "/toTag")
    public String toTag() {


        return "back/newback/article/tag_list";
    }


    //用户管理
    @Operation("进入用户管理界面")
    @Visitor(desc = "用户管理")
    @RequestMapping(path = "/toUserManager")
    public String toUserManager() {


        return "back/newback/article/userManager";
    }


    //友链管理
    @Operation("进入友链管理界面")
    @Visitor(desc = "友链管理")
    @RequestMapping(path = "/toLink")
    public String toLink() {


        return "back/newback/article/link_list";
    }

    //个人资料
    @Operation("进入个人资料界面")
    @Visitor(desc = "个人资料")
    @RequestMapping(path = "/touser")
    public String touser(HttpSession session,Model model) {

        String name = springSecurityUtil.currentUser(session);

        User user = userService.selectUserInfoByuserName(name);


        model.addAttribute("user",user);
        model.addAttribute("curName",name);
        model.addAttribute("commons", Commons.getInstance());
        model.addAttribute("bootstrap",new BootStrap());

        UserDetail userDetail = userDetailService.selectUserDetailByUserName(name);
        model.addAttribute("userDetail",userDetail);

        return "back/newback/article/user_list";
    }

    //访客记录
    @Operation("进入访客记录界面")
    @Visitor(desc = "访客记录")
    @RequestMapping(path = "/toVisitor")
    public String toVisitor() {


        return "back/newback/article/visitor_list";
    }

    //黑名单
    @Operation("进入黑名单界面")
    @Visitor(desc = "黑名单")
    @RequestMapping(path = "/toBlack")
    public String toBlack() {


        return "back/newback/article/black_list";
    }

    //拦截记录
    @Operation("进入拦截记录界面")
    @RequestMapping(path = "/toInterceptorLog")
    public String toInterceptorLog() {


        return "back/newback/article/interceptor_list";
    }

    //拦截记录数据
    @ResponseBody
    @RequestMapping(path = "/interceptData")
    public String interceptData(@RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "limit", defaultValue = "6")int limit){

        PageHelper.startPage(page,limit);
        List<Intercept> intercepts = interceptService.selectIntercepts();

        int count = interceptService.selectInterceptCount();

        LayuiData<Intercept> interceptlayuiData = new LayuiData<>();
        interceptlayuiData.setCode(0);
        interceptlayuiData.setMsg("");
        interceptlayuiData.setData(intercepts);
        interceptlayuiData.setCount(count);

        return JSON.toJSONString(interceptlayuiData);
    }


    //行为日志
    @Operation("进入行为日志界面")
    @RequestMapping(path = "/toLog")
    public String toLog() {


        return "back/newback/article/log_list";
    }

    //数据监控
    @Operation("进入数据监控界面")
    @RequestMapping(path = "/toMonitor")
    public String toMonitor() {


        return "back/newback/article/monitor";
    }

    //系统设置
    @Operation("进入系统设置界面")
    @RequestMapping(path = "/toSetting")
    public String toSetting(HttpSession session,Model model) {


        String username = springSecurityUtil.currentUser(session);
        Setting setting = settingService.selectUserSetting(username);
        UserDetail userDetail = userDetailService.selectUserDetailByUserName(username);


        model.addAttribute("userDetail", userDetail);
        model.addAttribute("themes", themes);
        model.addAttribute("setting", setting);
        model.addAttribute("commons", Commons.getInstance());
        model.addAttribute("bootstrap", new BootStrap());
        model.addAttribute("curName", username);

        return "back/newback/article/setting";
    }


}
