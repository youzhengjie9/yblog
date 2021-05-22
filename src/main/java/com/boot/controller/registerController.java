package com.boot.controller;

import com.boot.data.ResponseData.ArticleResponseData;
import com.boot.pojo.user;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class registerController {


    @RequestMapping(path = "/toregister")
    public String toRegister(){

        return "comm/register";
    }


    @RequestMapping(path = "/register")
    public String register(){

      return "comm/login";
    }








}


