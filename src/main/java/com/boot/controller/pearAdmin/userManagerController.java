package com.boot.controller.pearAdmin;

import com.alibaba.fastjson.JSON;
import com.boot.data.ResponseData.layuiData;
import com.boot.pojo.user;
import com.boot.service.userService;
import com.boot.utils.SpringSecurityUtil;
import com.github.pagehelper.PageHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.List;
@Controller("pearUserManagerController")
@RequestMapping(path = "/pear")
@CrossOrigin
public class userManagerController {

    @Autowired
    private userService userService;

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    private Logger logger=Logger.getLogger(userManagerController.class);



    @ResponseBody
    @RequestMapping(path = "/userManagerData")
    public String userManagerData(@RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "limit", defaultValue = "6") int limit,
                                  HttpSession session){

        layuiData<user> json=new layuiData<>();

        PageHelper.startPage(page,limit);
        List<user> users = userService.selectAllUser();
        String name = springSecurityUtil.currentUser(session);

        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        logger.debug(time + "   用户名：" + name + "进入后台用户管理页面");

        int total = userService.userCount();

        json.setCode(0);
        json.setCount(total); //总数
        json.setMsg("");
        json.setData(users); //分页数据

        return JSON.toJSONString(json);
    }

}
