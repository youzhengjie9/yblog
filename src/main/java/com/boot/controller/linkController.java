package com.boot.controller;

import com.boot.pojo.link;
import com.boot.pojo.userDetail;
import com.boot.pojo.visitor;
import com.boot.service.linkService;
import com.boot.service.userDetailService;
import com.boot.service.visitorService;
import com.boot.utils.Commons;
import com.boot.utils.SpringSecurityUtil;
import com.boot.utils.ipUtils;
import com.boot.utils.visitorUtil;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@Api(value = "友情链接控制器")
@RequestMapping(path = "/link")
public class linkController {
    @Autowired
    private linkService linkService;

    private Logger logger = Logger.getLogger(linkController.class);

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @Autowired
    private userDetailService userDetailService;

    private final int type = 1;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private visitorService visitorService;

    @GetMapping(path = "/list")
    public String to_Link(Model model, HttpSession session,HttpServletRequest request,@Value("进入友链管理") String desc) {

        //添加访客信息
        visitor visitor = visitorUtil.getVisitor(request, desc);
        String key = "visit_ip_" + visitor.getVisit_ip() + "_type_" + type;
        String s = (String) redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(s)) {
            visitorService.insertVisitor(visitor);
            //由ip和type组成的key放入redis缓存,5分钟内访问过的不再添加访客
            redisTemplate.opsForValue().set(key, "1", 60 * 5, TimeUnit.SECONDS);
        }

        String name = springSecurityUtil.currentUser(session);
        List<link> links = linkService.selectAllLink();
        userDetail userDetail = userDetailService.selectUserDetailByUserName(name);
        model.addAttribute("userDetail", userDetail);
        model.addAttribute("links", links);
        model.addAttribute("commons", Commons.getInstance());
        return "back/link_list";
    }

    @PostMapping(path = "/updateLink")
    public String updateLink(link link, Model model, HttpSession session, HttpServletRequest request) {

        linkService.updateLink(link);

        String username = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        String ipAddr = ipUtils.getIpAddr(request);
        logger.debug(time + "   用户名：" + username + "修改了友链：ip为" + ipAddr);
        List<link> links = linkService.selectAllLink();
        model.addAttribute("links", links);
        model.addAttribute("commons", Commons.getInstance());
        return "back/link_list";
    }


    @PostMapping(path = "/addLink")
    public String addLink(link link, Model model, HttpSession session, HttpServletRequest request) {

        linkService.insertLink(link);


        String username = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        String ipAddr = ipUtils.getIpAddr(request);
        logger.debug(time + "   用户名：" + username + "修改了友链：ip为" + ipAddr);
        List<link> links = linkService.selectAllLink();
        model.addAttribute("links", links);
        model.addAttribute("commons", Commons.getInstance());
        return "back/link_list";
    }

    @GetMapping(path = "/deleteLink")
    public String deleteLink(int id,Model model,HttpSession session,HttpServletRequest request){

        linkService.deleteLink(id);

        String username = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        String ipAddr = ipUtils.getIpAddr(request);
        logger.debug(time + "   用户名：" + username + "删除了友链：ip为" + ipAddr);
        List<link> links = linkService.selectAllLink();
        model.addAttribute("links", links);
        model.addAttribute("commons", Commons.getInstance());
        return "back/link_list";
    }



}
