package com.boot.controller;

import com.boot.utils.Commons;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/user")
public class userController {

    @RequestMapping(path = "/")
    public String toUserList(Model model){

        model.addAttribute("commons", Commons.getInstance());

        return "back/user_list";
    }

}
