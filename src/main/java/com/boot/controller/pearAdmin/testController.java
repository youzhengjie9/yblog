package com.boot.controller.pearAdmin;

import com.alibaba.fastjson.JSON;
import com.boot.controller.adminController;
import com.boot.pojo.Article;
import com.boot.pojo.setting;
import com.boot.pojo.tag;
import com.boot.pojo.userDetail;
import com.boot.service.*;
import com.boot.utils.Commons;
import com.boot.utils.SpringSecurityUtil;
import com.boot.utils.ipUtils;
import com.boot.utils.timeUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Api("调试接入新的后台管理系统控制器_仅仅是调试专用")
@Controller
@RequestMapping(path = "/pear")
public class testController {

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



    private void charts(Model model){
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
    }



    @RequestMapping(path = "/welcome")
    public String towelcome(){

        return "back/newback/article/welcome";
    }
    //控制后台，非常重要，访问后台时，会内嵌这个url
    @RequestMapping(path = "/toconsole")
    public String toconsole(Model model, HttpSession session, HttpServletRequest request){


        int usercount = userService.userCount(); //用户总数
        model.addAttribute("usercount",usercount);

        String username = springSecurityUtil.currentUser(session);
        setting setting = settingService.selectUserSetting(username);
        model.addAttribute("setting",setting); //传入系统设置

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
        model.addAttribute("recommendCount",articles1.size());



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
    //控制后台
    @RequestMapping(path = "/admin")
    public String toAdmin(){



        return "back/newback/index";
    }
    //登录
    @RequestMapping(path = "/login")
    public String tologin(){


        return "back/newback/login";
    }

    //文章
    //发布文章
    @RequestMapping(path = "/topublish")
    public String toPublishArticle(){


        return "back/newback/article/article_edit";
    }

    //文章管理
    @RequestMapping(path = "/toArticleManager")
    public String toArticleManager(){


        return "back/newback/article/article_list";
    }

    //分类管理
    @RequestMapping(path = "/toCategory")
    public String toCategory(){


        return "back/newback/article/categories";
    }

    //标签管理
    @RequestMapping(path = "/toTag")
    public String toTag(){


        return "back/newback/article/tag_list";
    }

    //附件管理
    @RequestMapping(path = "/toFileUpload")
    public String toFileUpload(){


        return "back/newback/article/img_list";
    }

    //爬取数据
    @RequestMapping(path = "/toCatchData")
    public String toCatchData(){


        return "back/newback/article/catch_list";
    }
    //用户管理
    @RequestMapping(path = "/toUserManager")
    public String toUserManager(){


        return "back/newback/article/userManager";
    }

    //友链管理
    @RequestMapping(path = "/toLink")
    public String toLink(){


        return "back/newback/article/link_list";
    }
    //个人资料
    @RequestMapping(path = "/touser")
    public String touser(){


        return "back/newback/article/user_list";
    }

    //访客记录
    @RequestMapping(path = "/toVisitor")
    public String toVisitor(){


        return "back/newback/article/visitor_list";
    }
    //黑名单
    @RequestMapping(path = "/toBlack")
    public String toBlack(){


        return "back/newback/article/black_list";
    }
//    //ip查询
//    @RequestMapping(path = "/toIpsearch")
//    public String toIpsearch(){
//
//
//        return "back/newback/system/user";
//    }
    //拦截记录
    @RequestMapping(path = "/toInterceptorLog")
    public String toInterceptorLog(){


        return "back/newback/article/interceptor_list";
    }
    //行为日志
    @RequestMapping(path = "/toLog")
    public String toLog(){


        return "back/newback/article/log_list";
    }

    //数据监控
    @RequestMapping(path = "/toMonitor")
    public String toMonitor(){


        return "back/newback/article/monitor";
    }
    //数据图表
    @RequestMapping(path = "/toEcharts")
    public String toEcharts(){


        return "back/newback/article/statistic_charts";
    }
    //系统设置
    @RequestMapping(path = "/toSetting")
    public String toSetting(){


        return "back/newback/article/setting";
    }





}
