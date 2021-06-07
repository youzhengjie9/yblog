package com.boot.controller;

import com.alibaba.fastjson.JSON;
import com.boot.pojo.*;
import com.boot.service.*;
import com.boot.utils.Commons;
import com.boot.utils.SpringSecurityUtil;
import com.boot.utils.visitorUtil;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.concurrent.TimeUnit;

/**
 * @author 游政杰
 * 2021/5/31
 */
@Controller
@RequestMapping(path = "/usermanager")
@Api("用户管理控制器")
public class usermanagerController {

    @Autowired
    private userService userService;

    @Autowired
    private userDetailService userDetailService;

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @Autowired
    private authorityService authorityService;

    private Logger logger= Logger.getLogger(usermanagerController.class);

    @Autowired
    private com.boot.service.visitorService visitorService;

    @Autowired
    private RedisTemplate redisTemplate;

    private final int type=2; //类型为1就是首页，类型为2就是后台管理

    @Autowired
    private userAuthorityService userAuthorityService;

    @RequestMapping(path = "/list")
    public String toUserManager(HttpSession session, Model model, HttpServletRequest request,@Value("进入用户管理界面") String desc){


        //添加访客信息
        visitor visitor = visitorUtil.getVisitor(request, desc);
        String key = "visit_ip_" + visitor.getVisit_ip() + "_type_" + type;
        String s = (String) redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(s)) {
            visitorService.insertVisitor(visitor);
            //由ip和type组成的key放入redis缓存,5分钟内访问过的不再添加访客
            redisTemplate.opsForValue().set(key, "1", 60 * 5, TimeUnit.SECONDS);
        }



        List<authority> authorities = authorityService.selectUserAuthority();
        model.addAttribute("auths",authorities);
        List<user> users = userService.selectAllUser();
        String name = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(name);
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

        user_authority user_authority = new user_authority();
        user_authority.setUser_id(id);
        user_authority.setAuthority_id(authorityid);
        userAuthorityService.changeUserAuthority(user_authority);


        List<authority> authorities = authorityService.selectUserAuthority();
        model.addAttribute("auths",authorities);
        List<user> users = userService.selectAllUser();
        String name = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(name);
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
