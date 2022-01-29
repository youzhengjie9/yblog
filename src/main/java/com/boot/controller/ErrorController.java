package com.boot.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Api("例如跳转重定向、转发页面")
@RequestMapping(path = "/err")
public class ErrorController {

    /**
     * ip被禁重定向的地址
     * @return
     */
    @GetMapping(path = "/black")
    public String toBlack(){

        return "error/black";
    }





}
