package com.boot.controller.oauth;

import com.alibaba.fastjson.JSONObject;
import com.boot.constant.GitHubConstant;
import com.boot.pojo.User;
import com.boot.service.AuthorityService;
import com.boot.service.RegisterService;
import com.boot.service.UserAuthorityService;
import com.boot.service.UserService;
import com.boot.utils.HttpClientUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author 游政杰
 */
@Controller
@RequestMapping(path = "/github")
@Api("GitHub第三方登录接口")
public class GithubController {

  private String clientId;

  private String clientSecret;

  private String redirectUri;

  @Autowired
  private UserService userService;

  @Autowired
  private UserAuthorityService userAuthorityService;

  @Autowired
  private AuthorityService authorityService;

  @Autowired
  private RegisterService registerService;

  @GetMapping("/callback")
  @ResponseBody
  public void callback(
      @RequestParam(name = "code") String code,
      @RequestParam(name = "state") String state,
      HttpServletRequest request,
      HttpServletResponse response)
      throws Exception {

    clientId = GitHubConstant.CLIENT_ID;
    clientSecret = GitHubConstant.CLIENT_SECRET;
    redirectUri = GitHubConstant.CALLBACK;

    try {

      String tokenUrl = GitHubConstant.TOKEN_URL;
      tokenUrl = tokenUrl.replace("CODE", code);
      String jsonToken = HttpClientUtils.doGet(tokenUrl, null);

      String token = jsonToken.split("&")[0].split("=")[1];

      String userInfoURL = GitHubConstant.USER_INFO_URL.replace("TOKEN", token);

      //    String userInfo = HttpClientUtils.doGet(userInfoURL);

      Map<String, String> header = new HashMap<>();
      header.put("accept", "application/json");
      String auth = "token " + token;
      header.put("Authorization", auth);
      String userInfo = HttpClientUtils.doGet("https://api.github.com/user", header);

      JSONObject jsonObject = (JSONObject) JSONObject.parse(userInfo);

      Integer id = (Integer) jsonObject.get("id");

      String myid = String.valueOf(id);

      String p = userService.selectPasswordByuserName(myid);
      if (p != null && !p.equals("")) { // 说明这是第二次第三方登录了，数据库已经有了记录

        // 如果不是第一次了，我们就要从数据库获取该用户的权限，并放入SpringSecurity的session中，否则将无法直接更改用户权限

        int userid = userService.selectUseridByUserName(myid);
        int authorityid = userAuthorityService.selectAuthorityID(userid);
        String authority = authorityService.selectAuthorityByid(authorityid); // 查询出来权限

        /** 逆向破解SpringSecurity验证,进行直接放行，绕过springSecurity验证 */
        HttpSession session = request.getSession();
        SecurityContextImpl securityContext = new SecurityContextImpl();
        String password = UUID.randomUUID().toString().replaceAll("-", ""); // 密码，防止用户直接进行登录，而不走第三方
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(myid, password, AuthorityUtils.createAuthorityList(authority));
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(
                user, password, AuthorityUtils.createAuthorityList(authority));
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetails(request));
        // 存放authentication到SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        securityContext.setAuthentication(usernamePasswordAuthenticationToken);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

      } else { // 如果是第一次的话，我们就给他注册一个帐号，这个帐号的名字是gitee的id，密码随机
        User user = new User();
        user.setUsername(myid);
        String password = UUID.randomUUID().toString().replaceAll("-", "");
        user.setPassword(password);
        registerService.register(user);

        HttpSession session = request.getSession();
        SecurityContextImpl securityContext = new SecurityContextImpl();
        org.springframework.security.core.userdetails.User u = new org.springframework.security.core.userdetails.User(myid, password, AuthorityUtils.createAuthorityList("ROLE_common"));
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(
                u, password, AuthorityUtils.createAuthorityList("ROLE_common"));
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetails(request));
        // 存放authentication到SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        securityContext.setAuthentication(usernamePasswordAuthenticationToken);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
      }
      response.sendRedirect("/");
    } catch (Exception e) {
      e.printStackTrace();
      response.sendRedirect("/loginPage");
    }
  }
}
