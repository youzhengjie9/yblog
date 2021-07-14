package com.boot.controller.oauth;

import com.alibaba.fastjson.JSONObject;
import com.boot.pojo.user;
import com.boot.service.authorityService;
import com.boot.service.registerService;
import com.boot.service.userAuthorityService;
import com.boot.service.userService;
import com.boot.utils.HttpUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Autowired
    private userService userService;

    @Autowired
    private registerService registerService;

    @Autowired
    private userAuthorityService userAuthorityService;

    @Autowired
    private authorityService authorityService;



    @RequestMapping("/callback")
    @ResponseBody
    public void callback(String code, Model model, HttpServletResponse response, HttpServletRequest request) throws Exception {
//        System.out.println("得到的code为：" + code);
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
//            System.out.println("得到的结果为：" + result);

            JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
            url = "https://gitee.com/api/v5/user";
            String getUserInfo = HttpUtil.get(url, jsonObject.get("access_token"));
//            System.out.println("得到的用户信息为：" + getUserInfo); //很重要

            JSONObject object = JSONObject.parseObject(getUserInfo);
            Integer id = (Integer) object.get("id"); //gitee id
//            String name = (String) object.get("name");// gitee 用户名
            String myid = String.valueOf(id);
            //把gitee的id变成名字存储起来


            String p = userService.selectPasswordByuserName(myid);
            if (p != null && !p.equals("")) { //说明这是第二次第三方登录了，数据库已经有了记录

                //如果不是第一次了，我们就要从数据库获取该用户的权限，并放入SpringSecurity的session中，否则将无法直接更改用户权限

                int userid = userService.selectUseridByUserName(myid);
                int authorityid = userAuthorityService.selectAuthorityID(userid);
                String authority = authorityService.selectAuthorityByid(authorityid); //查询出来权限

                /**
                 * 逆向破解SpringSecurity验证,进行直接放行，绕过springSecurity验证
                 */
                HttpSession session = request.getSession();
                SecurityContextImpl securityContext = new SecurityContextImpl();
                String password = UUID.randomUUID().toString().replaceAll("-", ""); //密码，防止用户直接进行登录，而不走第三方
                User user = new User(myid, password, AuthorityUtils.createAuthorityList(authority));
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, password, AuthorityUtils.createAuthorityList(authority));
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetails(request));
                //存放authentication到SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                securityContext.setAuthentication(usernamePasswordAuthenticationToken);
                session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

            } else { //如果是第一次的话，我们就给他注册一个帐号，这个帐号的名字是gitee的id，密码随机
                user user = new user();
                user.setUsername(myid);
                String password = UUID.randomUUID().toString().replaceAll("-", "");
                user.setPassword(password);
                registerService.register(user);

                HttpSession session = request.getSession();
                SecurityContextImpl securityContext = new SecurityContextImpl();
                User u = new User(myid, password, AuthorityUtils.createAuthorityList("ROLE_common"));
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(u, password, AuthorityUtils.createAuthorityList("ROLE_common"));
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetails(request));
                //存放authentication到SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                securityContext.setAuthentication(usernamePasswordAuthenticationToken);
                session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
            }


            jsonObject = (JSONObject) JSONObject.parse(getUserInfo);
            model.addAttribute("userName", jsonObject.get("name"));
            model.addAttribute("userImage", jsonObject.get("avatar_url"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 否则返回到登陆页面
        response.sendRedirect("http://localhost:80/");
    }


    @RequestMapping("/login")
    public String login() throws Exception {

        return "/comm/login";
    }


}
