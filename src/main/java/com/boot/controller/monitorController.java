package com.boot.controller;

import com.boot.pojo.img;
import com.boot.service.imgService;
import com.boot.utils.Commons;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "/monitor")
@Api("监控管理控制器")
public class monitorController {

    @Autowired
    private imgService imgService;

    @RequestMapping(path = "/list")
    public String toMonitor(Model model){
        List<img> imgs = imgService.selectAllImg();
        System.out.println(imgs);
        model.addAttribute("imgs",imgs);
        model.addAttribute("commons", Commons.getInstance());
        return "back/monitor";
    }





}
