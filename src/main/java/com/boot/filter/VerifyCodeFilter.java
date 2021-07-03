package com.boot.filter;

import com.alibaba.fastjson.JSON;
import com.boot.data.ResponseData.layuiJSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class VerifyCodeFilter extends OncePerRequestFilter {
    /**
     * 验证码过滤器
     */

    private String defaultFilterProcessUrl = "/login";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        if ("POST".equalsIgnoreCase(request.getMethod()) && defaultFilterProcessUrl.equals(request.getServletPath())) {

            // 登录请求校验验证码，非登录请求不用校验
            HttpSession session = request.getSession();
            String requestCaptcha = request.getParameter("captcha");
            //验证码的信息存放在seesion中
            String genCaptcha = (String) request.getSession().getAttribute("captcha");
            if (StringUtils.isEmpty(requestCaptcha)){
                //删除缓存里的验证码信息
                session.removeAttribute("captcha");
                throw new AuthenticationServiceException("验证码不能为空!");
            }
            if (!genCaptcha.toLowerCase().equals(requestCaptcha.toLowerCase())) {
                session.removeAttribute("captcha");
                throw new AuthenticationServiceException("验证码错误!");
            }
        }

        chain.doFilter(request, response);
    }
}
