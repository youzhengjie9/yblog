package com.boot.controller;

import com.boot.annotation.Visitor;
import com.boot.pojo.Link;
import com.boot.pojo.UserDetail;
import com.boot.service.LinkService;
import com.boot.service.UserDetailService;
import com.boot.service.VisitorService;
import com.boot.utils.Commons;
import com.boot.utils.SpringSecurityUtil;
import com.boot.utils.IpUtils;
import io.swagger.annotations.Api;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@Api(value = "友情链接控制器")
@RequestMapping(path = "/link")
public class LinkController {
    @Autowired
    private LinkService linkService;

    private Logger logger = Logger.getLogger(LinkController.class);

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @Autowired
    private UserDetailService userDetailService;

    private final int type = 1;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private VisitorService visitorService;

    @Visitor(desc = "进入友链管理")
    @GetMapping(path = "/list")
    public String to_Link(Model model, HttpSession session,HttpServletRequest request) {


        String name = springSecurityUtil.currentUser(session);
        List<Link> links = linkService.selectAllLink();
        UserDetail userDetail = userDetailService.selectUserDetailByUserName(name);
        model.addAttribute("userDetail", userDetail);
        model.addAttribute("links", links);
        model.addAttribute("commons", Commons.getInstance());
        return "back/link_list";
    }

    @PostMapping(path = "/updateLink")
    public String updateLink(Link link, Model model, HttpSession session, HttpServletRequest request) {

        linkService.updateLink(link);

        String username = springSecurityUtil.currentUser(session);
        UserDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        String ipAddr = IpUtils.getIpAddr(request);
        logger.debug(time + "   用户名：" + username + "修改了友链：ip为" + ipAddr);
        List<Link> links = linkService.selectAllLink();
        model.addAttribute("links", links);
        model.addAttribute("commons", Commons.getInstance());
        return "back/link_list";
    }


    @PostMapping(path = "/addLink")
    public String addLink(Link link, Model model, HttpSession session, HttpServletRequest request) {

        linkService.insertLink(link);


        String username = springSecurityUtil.currentUser(session);
        UserDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        String ipAddr = IpUtils.getIpAddr(request);
        logger.debug(time + "   用户名：" + username + "修改了友链：ip为" + ipAddr);
        List<Link> links = linkService.selectAllLink();
        model.addAttribute("links", links);
        model.addAttribute("commons", Commons.getInstance());
        return "back/link_list";
    }

    @GetMapping(path = "/deleteLink")
    public String deleteLink(int id,Model model,HttpSession session,HttpServletRequest request){

        linkService.deleteLink(id);

        String username = springSecurityUtil.currentUser(session);
        UserDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        String ipAddr = IpUtils.getIpAddr(request);
        logger.debug(time + "   用户名：" + username + "删除了友链：ip为" + ipAddr);
        List<Link> links = linkService.selectAllLink();
        model.addAttribute("links", links);
        model.addAttribute("commons", Commons.getInstance());
        return "back/link_list";
    }



}
