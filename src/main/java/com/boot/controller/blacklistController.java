package com.boot.controller;

import com.alibaba.fastjson.JSON;
import com.boot.annotation.Visitor;
import com.boot.constant.Constant;
import com.boot.data.ResponseData.ResponseJSON;
import com.boot.data.ResponseData.layuiJSON;
import com.boot.pojo.blacklist;
import com.boot.pojo.link;
import com.boot.pojo.userDetail;
import com.boot.pojo.visitor;
import com.boot.service.blacklistService;
import com.boot.service.userDetailService;
import com.boot.service.visitorService;
import com.boot.utils.*;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@Api("黑名单控制器")
@RequestMapping(path = "/black")
public class blacklistController {

    @Autowired
    private blacklistService blacklistService;

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    private Logger logger = Logger.getLogger(blacklistController.class);

    @Autowired
    private userDetailService userDetailService;

    private final int type = 1;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private visitorService visitorService;

    private final String key="intercept_ip_"; //拦截ip的key前缀

    @Visitor(desc = "进入黑名单管理")
    @GetMapping(path = "/list")
    public String toBlackList(Model model, HttpSession session, HttpServletRequest request) {

        List<blacklist> blacklists = blacklistService.selectBlackList();
        model.addAttribute("blacklists", blacklists);
        String username = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);
        model.addAttribute("commons", Commons.getInstance());
        return "back/black_list";
    }

    @GetMapping(path = "/deleteBlackList")
    public String deleteBlackList(String ip, Model model, HttpSession session, HttpServletRequest request) {
//        System.out.println(ip);
        blacklistService.deleteBlackListByIp(ip);
        redisTemplate.delete(key+ip); //从缓存中删除这个key

        List<blacklist> blacklists = blacklistService.selectBlackList();
        model.addAttribute("blacklists", blacklists);
        String username = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);
        model.addAttribute("commons", Commons.getInstance());
        return "back/black_list";
    }


    @PostMapping(path = "/addBlackList")
    public String addBlackList(String ip1, String ip2, String ip3, String ip4,
                               Model model, HttpSession session,
                               HttpServletRequest request) {
        String ip = ip1 + "." + ip2 + "." + ip3 + "." + ip4; //真正的ip
//        System.out.println(ip);
        blacklist blacklist = new blacklist();
        blacklist.setBlack_ip(ip);
        //通过前端传来的ip去获取地址
        String cityInfo = IpToAddressUtil.getCityInfo(ip);
//        if (StringUtils.isEmpty(cityInfo)) {
//            blacklist.setBlack_address("暂未检测到所在地址");
//        } else {
            blacklist.setBlack_address(cityInfo);
//        }
        blacklistService.addBlackList(blacklist);

        List<blacklist> blacklists = blacklistService.selectBlackList();
        model.addAttribute("blacklists", blacklists);
        String username = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);
        model.addAttribute("commons", Commons.getInstance());
        return "back/black_list";
    }


}
