package com.boot.controller;

import com.alibaba.fastjson.JSON;
import com.boot.pojo.*;
import com.boot.service.*;
import com.boot.utils.Commons;
import com.boot.utils.SpringSecurityUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Api(value = "统计图表控制器")
@RequestMapping(path = "/chart")
public class chartController {

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @Autowired
    private userDetailService userDetailService;

    @Autowired
    private articleService articleService;

    @Autowired
    private categoryService categoryService;

    @Autowired
    private tagService tagService;

    @Autowired
    private visitorService visitorService;

    @GetMapping(path = "/list")
    public String toStatisticCharts(HttpSession session, Model model) {


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

        //提供近7天内每一天的访问量
        List<onedayVisitor> onedayVisitors = visitorService.selectOneDayVisitor();
//        onedayVisitors.get(0).getDay()
//        onedayVisitors.get(0).getCount()
        model.addAttribute("onedayVisitors", onedayVisitors);

        String username = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);
        model.addAttribute("commons", Commons.getInstance());
        return "back/statistic_charts";
    }


}
