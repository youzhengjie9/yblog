package com.boot.controller.pearAdmin;

import com.alibaba.fastjson.JSON;
import com.boot.annotation.Operation;
import com.boot.annotation.Visitor;
import com.boot.data.ResponseData.LayuiJSON;
import com.boot.service.CatchDataService;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 游政杰
 * @Date 2021/6/30
 */
@Controller("pearCatchDataController")
@RequestMapping(path = "/pear")
public class CatchDataController {

    @Autowired
    private CatchDataService catchDataService;

    //爬取数据
    @Operation("进入爬取数据页面")
    @Visitor(desc = "爬取数据")
    @RequestMapping(path = "/toCatchData")
    public String toCatchData() {

        return "back/newback/article/catch_list";
    }

    /**
     * 抓取文章接口
     */
    @Operation("抓取文章数据")
    @RequestMapping(path = "/catch/Article")
    @ResponseBody
    public String catchArticle(String url, String code, HttpServletRequest request, Model model) {

        LayuiJSON json = new LayuiJSON();

        //验证码不正确
        if (!CaptchaUtil.ver(code, request)) {
            // 清除session中的验证码
            CaptchaUtil.clear(request);
            json.setMsg("验证码不正确");
            json.setSuccess(false);
            return JSON.toJSONString(json);
        } else { //验证码正确
            // 清除session中的验证码
            CaptchaUtil.clear(request);
            try {
                //抓取数据
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


    }

    /**
     * 抓取模块接口
     */
    @Operation("抓取模块中的文章数据")
    @RequestMapping(path = "/catch/ModelArticle")
    @ResponseBody
    public String catchModelArticle(String url, String code, HttpServletRequest request, Model model) {

        LayuiJSON json = new LayuiJSON();

        if (!CaptchaUtil.ver(code, request)) {
            // 清除session中的验证码
            CaptchaUtil.clear(request);
            json.setMsg("验证码不正确");
            json.setSuccess(false);
            return JSON.toJSONString(json);
        } else {
            // 清除session中的验证码
            CaptchaUtil.clear(request);
            try {
                //抓取数据
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


}
