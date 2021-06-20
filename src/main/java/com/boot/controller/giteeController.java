package com.boot.controller;

import com.alibaba.fastjson.JSONObject;
import com.boot.utils.HttpUtil;
import io.swagger.annotations.Api;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RequestMapping(path = "/gitee")
@Api("gitee第三方接口")
@Controller
public class giteeController {

    @Value("${gitee.oauth.CLIENTID}")
    public String CLIENTID;
    @Value("${gitee.oauth.CLIENTSECRET}")
    public String CLIENTSECRET;
    @Value("${gitee.oauth.callback}")
    public String URL;



    @RequestMapping("/callback")
    @ResponseBody
    public void callback(String code,Model model,HttpServletResponse response) throws Exception{
        System.out.println("得到的code为：" + code);
        Map<String, String> params = new HashMap<>(5);

        String url = "https://gitee.com/oauth/token";
        //申请应用时分配的AppKey
        params.put("client_id", CLIENTID);
        //申请应用时分配的AppSecret
        params.put("client_secret", CLIENTSECRET);
        //请求的类型，填写authorization_code
        params.put("grant_type", "authorization_code");
        //调用authorize获得的code值
        params.put("code", code);
        //回调地址，需需与注册应用里的回调地址一致。
        params.put("redirect_uri", URL);
        try {
            String result = HttpUtil.post(url, params);
            System.out.println("得到的结果为：" + result);

            JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
            url = "https://gitee.com/api/v5/user";
            String getUserInfo = HttpUtil.get(url, jsonObject.get("access_token"));
            System.out.println("得到的用户信息为：" + getUserInfo);
            jsonObject = (JSONObject) JSONObject.parse(getUserInfo);
            model.addAttribute("userName", jsonObject.get("name"));
            model.addAttribute("userImage", jsonObject.get("avatar_url"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 否则返回到登陆页面
        response.sendRedirect("http://localhost:8080/");
    }



    @RequestMapping("/login")
    public String login() throws Exception{

        return "/comm/login";// TODO 修改成自己需要返回的页面...
    }



}
