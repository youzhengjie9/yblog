package com.boot.controller;

import com.alibaba.fastjson.JSON;
import com.boot.constant.Constant;
import com.boot.data.ResponseData.ArticleResponseData;
import com.boot.data.ResponseData.ResponseJSON;
import com.boot.utils.Commons;
import com.boot.utils.jsoupUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@Api("抓取文章")
@RequestMapping(path = "/catchArticle")
public class catchArticleController {

    private static final List<String> selects = new ArrayList<>(); //存放爬虫选项

    static {

        selects.add("csdn");

    }

    @Autowired
    private jsoupUtils jsoupUtils;

    @GetMapping(path = "/list")
    public String toCatchArticleList(Model model) {
        model.addAttribute("selects", selects);
        model.addAttribute("commons", Commons.getInstance());
        return "back/catch_list";
    }

    /**
     * 抓取文章接口
     */
    @RequestMapping(path = "/catchArticle")
    @ResponseBody
    public String catchArticle(String url, String type, Model model) {

        if (type != null && type.equals("csdn")) {
            try {
                jsoupUtils.catchData_csdn(url);
                ResponseJSON responseJSON = new ResponseJSON();
                responseJSON.setData("");
                responseJSON.setResult(Constant.SUCCESS);
                return JSON.toJSONString(responseJSON);
            } catch (Exception e) {
                e.printStackTrace();
                ResponseJSON responseJSON = new ResponseJSON();
                responseJSON.setData("");
                responseJSON.setResult(Constant.ERROR);
                return JSON.toJSONString(responseJSON);
            }
        } else {
            ResponseJSON responseJSON = new ResponseJSON();
            responseJSON.setData("");
            responseJSON.setResult(Constant.ERROR);
            return JSON.toJSONString(responseJSON);
        }


    }

    /**
     * 抓取模块接口
     */
    @PostMapping(path = "/catchModelArticle")
    @ResponseBody
    public String catchModelArticle(String modelUrl, Model model) {

        try {
            jsoupUtils.batchCatchArticleByModel_csdn(modelUrl);
            ResponseJSON responseJSON = new ResponseJSON();
            responseJSON.setResult(Constant.SUCCESS);
            return JSON.toJSONString(responseJSON);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseJSON responseJSON = new ResponseJSON();
            responseJSON.setResult(Constant.ERROR);
            return JSON.toJSONString(responseJSON);
        }

    }


}
