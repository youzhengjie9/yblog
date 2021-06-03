package com.boot.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Api("注销控制器")
public class logoutController {


    @RequestMapping(path = "/logout")
    public void logout() {

    }


}
