package com.boot.controller;

import com.alibaba.fastjson.JSON;
import com.boot.service.articleService;
import com.boot.service.userService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * @author 游政杰
 */
@Controller
@RequestMapping(path = "/article")
@Api("文章控制器")
public class articleController {

    @Autowired
    private articleService articleService;

    @GetMapping(path = "/updateAllowComment")
    @ResponseBody
    @ApiOperation(value = "修改是否可以评论",notes = "修改文章是否可以评论")
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
