package com.boot.controller;

import com.boot.pojo.img;
import com.boot.pojo.userDetail;
import com.boot.service.imgService;
import com.boot.service.userDetailService;
import com.boot.utils.Commons;
import com.boot.utils.SpringSecurityUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
/**
 * @author 游政杰
 * 2021/5/31 21:25
 */
@Controller
@RequestMapping(path = "/monitor")
@Api("监控管理控制器")
public class monitorController {

    @Autowired
    private imgService imgService;

    @Autowired
    private userDetailService userDetailService;

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @RequestMapping(path = "/list")
    public String toMonitor(Model model, HttpSession session){

        String username = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail",userDetail);
        model.addAttribute("commons", Commons.getInstance());
        return "back/monitor";
    }





}
