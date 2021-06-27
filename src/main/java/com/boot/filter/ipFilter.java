package com.boot.filter;

import com.boot.pojo.intercept;
import com.boot.service.blacklistService;
import com.boot.service.interceptService;
import com.boot.utils.IpToAddressUtil;
import com.boot.utils.browserOS;
import com.boot.utils.ipUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ipFilter implements Filter {

    @Autowired
    private blacklistService blacklistService;

    @Autowired
    private interceptService interceptService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("filter init===========");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        //获取当前ip地址
        String ipAddr = ipUtils.getIpAddr((HttpServletRequest) servletRequest);
//        System.out.println(ipAddr);
        boolean var = blacklistService.checkIpHasBlack(ipAddr);
        if(var){ //如果是黑名单
            //添加拦截记录
            String address = IpToAddressUtil.getCityInfo(ipAddr);
            String browserName = browserOS.getBrowserName((HttpServletRequest) servletRequest);
            String osName = browserOS.getOsName((HttpServletRequest) servletRequest);
            Date date = new Date();
            java.sql.Date sqlData = new java.sql.Date(date.getTime());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = simpleDateFormat.format(sqlData);
            intercept intercept = new intercept(); //封装拦截信息
            intercept.setIntercept_ip(ipAddr);
            intercept.setIntercept_address(address);
            intercept.setIntercept_browser(browserName);
            intercept.setIntercept_os(osName);
            intercept.setIntercept_time(dateTime);
            intercept.setDesc("用户ip在黑名单");
            System.out.println(intercept);
            interceptService.insertIntercept(intercept);

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
