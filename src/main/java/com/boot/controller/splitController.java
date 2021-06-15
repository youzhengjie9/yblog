package com.boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(path = "/sliderCaptcha")
public class splitController {

    @RequestMapping(path = "/tocapcha")
    public String toCaptcha(){

        return "/client/captcha";
    }


    @RequestMapping(path = "/checkSuccess")
    public String checkSuccess(){


        return "/client/captcha";
    }

}
