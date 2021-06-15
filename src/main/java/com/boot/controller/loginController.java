package com.boot.controller;

import com.alibaba.fastjson.JSON;
import com.boot.constant.Constant;
import com.boot.dao.articleMapper;
import com.boot.data.ResponseData.ResponseJSON;
import com.boot.pojo.Article;
import com.boot.pojo.userDetail;
import com.boot.service.userDetailService;
import com.boot.utils.Commons;
import com.boot.utils.ipUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author 游政杰
 * 2021/5/20
 */
@Controller
@Api("登录控制器")
public class loginController {

//    @Autowired
//    private articleMapper articleMapper;

    @Autowired
    private userDetailService userDetailService;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(path = "/loginPage")
    public String toLoginPage(Model model,HttpServletRequest request) {

        String ipAddr = ipUtils.getIpAddr(request);
        Object o = redisTemplate.opsForValue().get(ipAddr + "_lg");
        model.addAttribute("ch",o);
        return "comm/login";
    }

    @RequestMapping(path = "/LoginfailPage")
    public String failPage(Model model) {
        model.addAttribute("error", 1);
        return "comm/login";
    }


    @RequestMapping(path = "/login")
    public void login(String username, String password, HttpServletResponse response, HttpServletRequest request) throws IOException {

        response.sendRedirect("/");
    }

    @RequestMapping(path = "/check")
    @ResponseBody
    public String checkUsername(String username) {
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        if (userDetail != null) { //成功
            ResponseJSON responseJSON = new ResponseJSON();
            responseJSON.setData(userDetail.getIcon()); //这个数据我传的是头像
            responseJSON.setResult(Constant.SUCCESS);
            String json = JSON.toJSONString(responseJSON);
            return json;
        } else {        //失败
            ResponseJSON responseJSON = new ResponseJSON();
            responseJSON.setResult(Constant.ERROR);
            String json = JSON.toJSONString(responseJSON);
            return json;
        }

    }

}
