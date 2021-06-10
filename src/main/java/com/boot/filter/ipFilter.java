package com.boot.filter;

import com.boot.service.blacklistService;
import com.boot.utils.ipUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ipFilter implements Filter {

    @Autowired
    private blacklistService blacklistService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("filter init===========");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        //获取当前ip地址
        String ipAddr = ipUtils.getIpAddr((HttpServletRequest) servletRequest);
        System.out.println(ipAddr);
        boolean var = blacklistService.checkIpHasBlack(ipAddr);
        if(var){ //如果是黑名单

//           HttpServletResponse httpServletResponse=   (HttpServletResponse)servletResponse;
//
//           httpServletResponse.sendRedirect("/err/black");

            HttpServletRequest request=(HttpServletRequest)servletRequest;

            request.getRequestDispatcher("/err/black").forward(servletRequest, servletResponse);

        }else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        System.out.println("filter destroy ==============");
    }
}
