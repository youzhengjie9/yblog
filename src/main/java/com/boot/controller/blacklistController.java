package com.boot.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping(path = "/list")
    public String toBlackList(Model model, HttpSession session, HttpServletRequest request, @Value("进入黑名单管理") String desc) {

        //添加访客信息
        visitor visitor = visitorUtil.getVisitor(request, desc);
        String key = "visit_ip_" + visitor.getVisit_ip() + "_type_" + type;
        String s = (String) redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(s)) {
            visitorService.insertVisitor(visitor);
            //由ip和type组成的key放入redis缓存,5分钟内访问过的不再添加访客
            redisTemplate.opsForValue().set(key, "1", 60 * 5, TimeUnit.SECONDS);
        }


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
        if (StringUtils.isEmpty(cityInfo)) {
            blacklist.setBlack_address("暂未检测到所在地址");
        } else {
            blacklist.setBlack_address(cityInfo);
        }
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
