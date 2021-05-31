package com.boot.controller;

import com.boot.utils.Commons;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/monitor")
public class monitorController {


    @RequestMapping(path = "/list")
    public String toMonitor(Model model){

        model.addAttribute("commons", Commons.getInstance());
        return "back/monitor";
    }





}
