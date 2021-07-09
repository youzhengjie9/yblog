package com.boot.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.boot.constant.loginType;
import com.boot.pojo.loginLog;
import com.boot.service.LoginLogService;
import com.boot.service.authorityService;
import com.boot.service.userAuthorityService;
import com.boot.service.userService;
import com.boot.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class RememberInterceptor implements HandlerInterceptor {

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    private final String key = "@%&^=*remember-yblog=@#&%"; //密钥，切勿泄露出去

    private final String REMEMBER_KEY = "REMEMBER_"; //记住我的Redis key前缀

    @Autowired
    private com.boot.service.userService userService;

    @Autowired
    private com.boot.service.userAuthorityService userAuthorityService;

    @Autowired
    private com.boot.service.authorityService authorityService;

    @Autowired
    private LoginLogService loginLogService;

    /**
     * 记住我拦截器类
     *
     * @param session
     * @param request
     */

    private void autoLoginByRemember(HttpSession session, HttpServletRequest request) {

        try { //判断当前ip的用户是否登录
            String s = springSecurityUtil.currentUser(session);

        } catch (Exception e) {
            //异常了就是没有登录，没有登录就要检查redis中有没有该ip的记住我记录
//            System.out.println("没有登录");

            Object token = (String) redisTemplate.opsForValue().get(REMEMBER_KEY + ipUtils.getIpAddr(request));

            //解析token
            if (token == null || token.equals("")) {//被删除了或者过期了
                System.out.println("请重新认证");
            } else {
                try {
                    String json = AesUtil.aesDecrypt((String) token, key);
                    JSONObject jsonObject = JSONObject.parseObject(json);
                    String username = (String) jsonObject.get("username");
                    String password = (String) jsonObject.get("password");

                    if (userService.selectPasswordByuserName(username).equals(password)) { //验证成功

                        /**
                         * 逆向破解SpringSecurity验证,进行直接放行，绕过springSecurity验证
                         */
                        int userid = userService.selectUseridByUserName(username);
                        int authorityid = userAuthorityService.selectAuthorityID(userid);
                        String authority = authorityService.selectAuthorityByid(authorityid); //查询出来权限

                        SecurityContextImpl securityContext = new SecurityContextImpl();
                        User user = new User(username, password, AuthorityUtils.createAuthorityList(authority));
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, password, AuthorityUtils.createAuthorityList(authority));
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetails(request));
                        //存放authentication到SecurityContextHolder
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        securityContext.setAuthentication(usernamePasswordAuthenticationToken);
                        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

                        //登录日志，走拦截器的登录类型都是记住我类型
                        String ipAddr = ipUtils.getIpAddr(request);
                        loginLog loginLog = new loginLog();
                        loginLog.setIp(ipAddr);
                        loginLog.setAddress(IpToAddressUtil.getCityInfo(ipAddr));
                        loginLog.setBrowser(browserOS.getBrowserName(request));
                        loginLog.setOs(browserOS.getOsName(request));
                        loginLog.setUsername(username);
                        Date d = new Date();
                        java.sql.Date date=new java.sql.Date(d.getTime());
                        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String t = fm.format(date);
                        loginLog.setTime(t);
                        loginLog.setType(loginType.REMEMBER_LOGIN); //登录类型为2
                        loginLogService.insertLoginLog(loginLog);

                    } else {
                        System.out.println("请重新认证");
                    }
                } catch (Exception ex) {
                    System.out.println("请重新认证");
                }
            }
        }

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        this.autoLoginByRemember(session, request);

        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
