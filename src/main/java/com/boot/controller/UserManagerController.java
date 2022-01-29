package com.boot.controller;

import com.alibaba.fastjson.JSON;
import com.boot.annotation.Visitor;
import com.boot.pojo.*;
import com.boot.service.*;
import com.boot.utils.Commons;
import com.boot.utils.SpringSecurityUtil;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author 游政杰
 * 2021/5/31
 */
@Controller
@RequestMapping(path = "/usermanager")
@Api("用户管理控制器")
public class UserManagerController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @Autowired
    private AuthorityService authorityService;

    private Logger logger= Logger.getLogger(UserManagerController.class);

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private RedisTemplate redisTemplate;

    private final int type=1;

    @Autowired
    private UserAuthorityService userAuthorityService;

    @Visitor(desc = "进入用户管理界面")
    @RequestMapping(path = "/list")
    public String toUserManager(HttpSession session, Model model, HttpServletRequest request){


        List<Authority> authorities = authorityService.selectUserAuthority();
        model.addAttribute("auths",authorities);
        List<User> users = userService.selectAllUser();
        String name = springSecurityUtil.currentUser(session);
        UserDetail userDetail = userDetailService.selectUserDetailByUserName(name);
        model.addAttribute("userDetail",userDetail);
        model.addAttribute("users",users);
        model.addAttribute("commons", Commons.getInstance());

        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        logger.debug(time + "   用户名：" + name + "进入后台用户管理页面");


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
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        logger.debug(time + "   用户名：" + name + "进入后台把用户"+name+"进行失效处理");
        return JSON.toJSONString("ajax success");
    }

    @PostMapping(path = "/updateUser")
    public String updateUser(int id,
                             int authorityid,
                             String email,
                             HttpSession session,
                             Model model){

        System.out.println(id +"====> "+authorityid+"==>"+email);

        if(!StringUtils.isEmpty(email)){
            userService.updateUserForEmail(id, email); //修改email
        }

        UserAuthority userAuthority = new UserAuthority();
        userAuthority.setUser_id(id);
        userAuthority.setAuthority_id(authorityid);
        userAuthorityService.changeUserAuthority(userAuthority);


        List<Authority> authorities = authorityService.selectUserAuthority();
        model.addAttribute("auths",authorities);
        List<User> users = userService.selectAllUser();
        String name = springSecurityUtil.currentUser(session);
        UserDetail userDetail = userDetailService.selectUserDetailByUserName(name);
        model.addAttribute("userDetail",userDetail);
        model.addAttribute("users",users);
        model.addAttribute("commons", Commons.getInstance());
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        logger.debug(time + "   用户名：" + name + "进入后台修改用户id为"+id+"的用户");
        return "back/userManager";
    }






}
