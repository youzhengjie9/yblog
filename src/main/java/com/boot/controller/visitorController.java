package com.boot.controller;

import com.alibaba.fastjson.JSON;
import com.boot.annotation.Visitor;
import com.boot.constant.Constant;
import com.boot.data.ResponseData.ResponseJSON;
import com.boot.pojo.userDetail;
import com.boot.pojo.visitor;
import com.boot.service.userDetailService;
import com.boot.service.visitorService;
import com.boot.utils.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping(path = "/visitor")
@Api(value = "访客控制器")
public class visitorController {

    private final int type = 1;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private visitorService visitorService;

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @Autowired
    private userDetailService userDetailService;


    @Visitor(desc = "进入访客管理")
    @GetMapping(path = "/list")
    public String toVisitorList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, Model model,
                                HttpServletRequest request
            , HttpSession session) {


        PageHelper.startPage(pageNum, 8);
        List<com.boot.pojo.visitor> visitors = visitorService.selectVisitor();

        PageInfo pageInfo = new PageInfo(visitors);

        String username = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);

        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("visitors", visitors);
        model.addAttribute("commons", Commons.getInstance());
        return "back/visitor_list";
    }


    @GetMapping(path = "/ipsearchList")
    public String toIpSearchList(Model model,
                                 HttpServletRequest request, HttpSession session) {


        String username = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail", userDetail);
        model.addAttribute("commons", Commons.getInstance());
        model.addAttribute("bootstrap", new bootstrap());
        return "back/ipsearch";
    }

    @RequestMapping(path = "/searchIpaddress")
    @ResponseBody
    public String searchIpAddress(String ip1, String ip2, String ip3, String ip4) {
        String ip = ip1 + "." + ip2 + "." + ip3 + "." + ip4; //真正的ip
        System.out.println(ip);

        String cityInfo = IpToAddressUtil.getCityInfo(ip);

        ResponseJSON responseJSON = new ResponseJSON();

        responseJSON.setData(cityInfo);

        responseJSON.setResult(Constant.SUCCESS);
        return JSON.toJSONString(responseJSON);
    }
}
