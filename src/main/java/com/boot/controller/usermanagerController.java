package com.boot.controller;

import com.alibaba.fastjson.JSON;
import com.boot.pojo.authority;
import com.boot.pojo.user;
import com.boot.pojo.userDetail;
import com.boot.pojo.user_authority;
import com.boot.service.authorityService;
import com.boot.service.userAuthorityService;
import com.boot.service.userDetailService;
import com.boot.service.userService;
import com.boot.utils.Commons;
import com.boot.utils.SpringSecurityUtil;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(path = "/usermanager")
public class usermanagerController {

    @Autowired
    private userService userService;

    @Autowired
    private userDetailService userDetailService;

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @Autowired
    private authorityService authorityService;

    @Autowired
    private userAuthorityService userAuthorityService;

    @RequestMapping(path = "/list")
    public String toUserManager(HttpSession session, Model model){


        List<authority> authorities = authorityService.selectUserAuthority();
        model.addAttribute("authorities",authorities);
        List<user> users = userService.selectAllUser();
        String name = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(name);
        model.addAttribute("userDetail",userDetail);
        model.addAttribute("users",users);
        model.addAttribute("commons", Commons.getInstance());
        return "back/userManager";
    }

    @GetMapping(path = "/updateVaild")
    @ResponseBody
    public String updateVaild(String valid,
                              String name,
                              HttpSession session){
        String username = springSecurityUtil.currentUser(session);
        int i = Integer.parseInt(valid);
        if(i==0){
             userService.updateValidTo_1(name);
         }else if (i==1){
            userService.updateValidTo_0(name);
         }

//        System.out.println(valid+"  ====>" +name);

        return JSON.toJSONString("ajax success");
    }

    @PostMapping(path = "/updateUser")
    public String updateUser(int id,
                             int authorityid,
                             String email,
                             HttpSession session,
                             Model model){
        if(!StringUtils.isEmpty(email)){
            userService.updateUserForEmail(id, email); //修改email
        }

        user_authority user_authority = new user_authority();
        user_authority.setUser_id(id);
        user_authority.setAuthority_id(authorityid);
        userAuthorityService.changeUserAuthority(user_authority);


        List<authority> authorities = authorityService.selectUserAuthority();
        model.addAttribute("authorities",authorities);
        List<user> users = userService.selectAllUser();
        String name = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(name);
        model.addAttribute("userDetail",userDetail);
        model.addAttribute("users",users);
        model.addAttribute("commons", Commons.getInstance());
        return "back/userManager";
    }






}
