package com.boot.controller;

import com.boot.annotation.Visitor;
import com.boot.constant.themeConstant;
import com.boot.pojo.*;
import com.boot.service.*;
import com.boot.utils.Commons;
import com.boot.utils.SpringSecurityUtil;
import com.boot.utils.cssUtil;
import com.boot.utils.visitorUtil;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author 游政杰
 * 2021/6/4
 */

@Api(value = "文章归档控制器")
@Controller
@RequestMapping(path = "/archive")
public class archiveController {

    @Autowired
    private SpringSecurityUtil securityUtil;

    @Autowired
    private userDetailService userDetailService;

    @Autowired
    private linkService linkService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private articleService articleService;

    @Autowired
    private archiveService archiveService;

    @Autowired
    private visitorService visitorService;

    @Autowired
    private cssUtil cssUtil;

    private final int type = 1;

    @Autowired
    private tagService tagService;


    @Autowired
    private settingService settingService;

    //前10排行
    private static final List<Article> ArticleOrder_10(List<Article> articleList) {
        List<Article> list = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            list.add(articleList.get(i));
        }
        return list;
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

    /**
     * ====需要进行优化，此处性能差
     */
    @Visitor(desc = "去归档页面")
    @GetMapping(path = "/list")
    @ApiOperation(value = "去归档页面")
    public ModelAndView toArchiveList(HttpSession session, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();

        this.setting(session,modelAndView);

        //跳转不同页面主题判断
        if (themeConstant.curTheme.equals(themeConstant.CALM_THEME)) { //calm主题
            modelAndView.setViewName("client/index2"); //跳转页面
            modelAndView.addObject("archiveAc","active");
            List<tag> tags = tagService.selectTags_limit8();
            modelAndView.addObject("tags", tags);

        } else if (themeConstant.curTheme.equals(themeConstant.DEFAULT_THEME)) { //默认主题
            modelAndView.setViewName("client/index"); //跳转页面

        }


        //前端进行判断，isArchive是不是等于空，如果不是就是归档页面，进行页面代码的复用，省去写一个新的页面
        modelAndView.addObject("isArchive", "true");

        List<archive> archives = archiveService.selectAllArchiveGroup(); //获取归档分组信息
//        modelAndView.addObject("archives",archives);
        modelAndView.addObject("cssUtil", cssUtil);



        /**
         * 可以通过hashMap来维护归档数据
         * key=年月分组
         * value=List集合，存储对应的分组出来的数据
         */
        Map<String, List<Article>> concurrentHashMap = new ConcurrentHashMap<>();
        for (archive archive : archives) {
            String months = archive.getMonths();
            List<Article> articles = archiveService.selectArticleByarchiveTime(months);
            concurrentHashMap.put(months, articles);
        }
        modelAndView.addObject("archiveMap", concurrentHashMap); //传入前端


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


        //推荐文章
        PageHelper.startPage(1,5);
        List<Article> recommends = articleService.selectArticleByRecommend();
        modelAndView.addObject("recommends",recommends);

        //友链
        List<link> links = linkService.selectAllLink();
        modelAndView.addObject("links", links);
        modelAndView.addObject("commons", Commons.getInstance());

        return modelAndView;
    }


}
