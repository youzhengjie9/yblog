package com.boot.controller;

import com.alibaba.fastjson.JSON;
import com.boot.service.articleService;
import com.boot.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/article")
public class articleController {

    @Autowired
    private articleService articleService;

    @GetMapping(path = "/updateAllowComment")
    @ResponseBody
    public String updateAllowComment(int id, String allow) {
        if (allow.equals("false")) {
            articleService.updateAllowCommentTo_1(id);
        } else if (allow.equals("true")) {
            articleService.updateAllowCommentTo_0(id);
        }
        String s = JSON.toJSONString("success");
        return s;
    }


}
