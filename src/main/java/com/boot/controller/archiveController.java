package com.boot.controller;

import com.boot.pojo.*;
import com.boot.service.*;
import com.boot.utils.Commons;
import com.boot.utils.SpringSecurityUtil;
import com.boot.utils.cssUtil;
import com.boot.utils.visitorUtil;
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

    private final int type=1;

    //前10排行
    private static final List<Article> ArticleOrder_10(List<Article> articleList) {
        List<Article> list = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            list.add(articleList.get(i));
        }
        return list;
    }

    @GetMapping(path = "/list")
    @ApiOperation(value = "去归档页面")
    public ModelAndView toArchiveList(HttpSession session, HttpServletRequest request, @Value("归档页面") String desc){
        ModelAndView modelAndView = new ModelAndView();
        //前端进行判断，isArchive是不是等于空，如果不是就是归档页面，进行页面代码的复用，省去写一个新的页面
        modelAndView.addObject("isArchive","true");

        List<archive> archives = archiveService.selectAllArchiveGroup(); //获取归档分组信息
//        modelAndView.addObject("archives",archives);
        modelAndView.addObject("cssUtil",cssUtil);

        //添加访客信息
        visitor visitor = visitorUtil.getVisitor(request, desc);
        String key1 = "visit_ip_" + visitor.getVisit_ip() + "_type_" + type;
        String s = (String) redisTemplate.opsForValue().get(key1);
        if (StringUtils.isEmpty(s)) {
            visitorService.insertVisitor(visitor);
            //由ip和type组成的key放入redis缓存,5分钟内访问过的不再添加访客
            redisTemplate.opsForValue().set(key1, "1", 60 * 5, TimeUnit.SECONDS);
        }


        /**
         * 可以通过hashMap来维护归档数据
         * key=年月分组
         * value=List集合，存储对应的分组出来的数据
         */
        Map<String, List<Article>> concurrentHashMap = new ConcurrentHashMap<>();
        for (archive archive : archives) {
            String months = archive.getMonths();
            List<Article> articles = archiveService.selectArticleByarchiveTime(months);
            concurrentHashMap.put(months,articles);
        }
        modelAndView.addObject("archiveMap",concurrentHashMap); //传入前端


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


        //友链
        List<link> links = linkService.selectAllLink();
        modelAndView.addObject("links",links);
        modelAndView.addObject("commons", Commons.getInstance());
        modelAndView.setViewName("client/index");
        return modelAndView;
    }







}