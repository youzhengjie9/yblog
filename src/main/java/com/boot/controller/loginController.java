package com.boot.controller;

import com.boot.dao.articleMapper;
import com.boot.pojo.Article;
import com.boot.utils.Commons;
import com.boot.utils.ipUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @Autowired
    private articleMapper articleMapper;

    @RequestMapping(path = "/loginPage")
    public String toLoginPage() {

        return "comm/login";
    }

    @RequestMapping(path = "/LoginfailPage")
    public String failPage(Model model) {
        model.addAttribute("error",1);
        return "comm/login";
    }


    @RequestMapping(path = "/login")
    public void login(String username, String password, HttpServletResponse response,HttpServletRequest request) throws IOException {

        response.sendRedirect("/");
    }


}
