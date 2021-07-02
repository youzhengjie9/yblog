package com.boot.controller.pearAdmin;

import com.alibaba.fastjson.JSON;
import com.boot.annotation.Visitor;
import com.boot.constant.Constant;
import com.boot.data.ResponseData.ResponseJSON;
import com.boot.data.ResponseData.layuiJSON;
import com.boot.service.catchDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 游政杰
 * @Date 2021/6/30
 */
@Controller("pearCatchDataController")
@RequestMapping(path = "/pear")
public class catchDataController {

    @Autowired
    private catchDataService catchDataService;

    //爬取数据
    @Visitor(desc = "爬取数据")
    @RequestMapping(path = "/toCatchData")
    public String toCatchData() {

        return "back/newback/article/catch_list";
    }

    /**
     * 抓取文章接口
     */
    @RequestMapping(path = "/catch/Article")
    @ResponseBody
    public String catchArticle(String url,String code, Model model) {

        layuiJSON json = new layuiJSON();



        try {
            catchDataService.catchData_csdn(url);

            json.setMsg("抓取CSDN文章数据成功");
            json.setSuccess(true);
            return JSON.toJSONString(json);
        } catch (Exception e) {
            e.printStackTrace();
            json.setMsg("抓取CSDN文章数据失败");
            json.setSuccess(false);
            return JSON.toJSONString(json);
        }

    }

    /**
     * 抓取模块接口
     */
    @RequestMapping(path = "/catch/ModelArticle")
    @ResponseBody
    public String catchModelArticle(String url,String code, Model model) {

        layuiJSON json = new layuiJSON();

        try {
            catchDataService.batchCatchArticleByModel_csdn(url);
            json.setMsg("抓取CSDN模块数据成功");
            json.setSuccess(true);
            return JSON.toJSONString(json);
        } catch (Exception e) {
            e.printStackTrace();
            json.setMsg("抓取CSDN模块数据失败");
            json.setSuccess(false);
            return JSON.toJSONString(json);
        }

    }


}
