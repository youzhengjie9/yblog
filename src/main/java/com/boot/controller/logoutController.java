package com.boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class logoutController {


    @RequestMapping(path = "/logout")
    public void logout() {

    }


}
