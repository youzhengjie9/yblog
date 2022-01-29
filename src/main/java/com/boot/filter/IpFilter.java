package com.boot.filter;

import com.boot.pojo.Intercept;
import com.boot.service.BlackListService;
import com.boot.service.InterceptService;
import com.boot.utils.IpToAddressUtil;
import com.boot.utils.BrowserOS;
import com.boot.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class IpFilter implements Filter {

    @Autowired
    private BlackListService blacklistService;

    @Autowired
    private InterceptService interceptService;

    @Autowired
    private RedisTemplate redisTemplate;

    private final String key="intercept_ip_"; //拦截ip的key前缀

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("filter init===========");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        //获取当前ip地址
        String ipAddr = IpUtils.getIpAddr((HttpServletRequest) servletRequest);
//        System.out.println(ipAddr);
        boolean var = blacklistService.checkIpHasBlack(ipAddr);
        if(var){ //如果是黑名单

            //防止黑名单用户不断访问网站，而拦截记录一直会添加到mysql中
            //设置一个缓存，一个黑名单用户在一定时间内的多次操作被拦截只会添加一个记录到mysql中
//            redisTemplate.opsForValue().set(key+ipAddr,"996",60*15, TimeUnit.SECONDS);

            Object o = redisTemplate.opsForValue().get(key + ipAddr);
            if(o==null){
                //第一次访问，可以添加记录
                //添加拦截记录
                String address = IpToAddressUtil.getCityInfo(ipAddr);
                String browserName = BrowserOS.getBrowserName((HttpServletRequest) servletRequest);
                String osName = BrowserOS.getOsName((HttpServletRequest) servletRequest);
                Date date = new Date();
                java.sql.Date sqlData = new java.sql.Date(date.getTime());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateTime = simpleDateFormat.format(sqlData);
                Intercept intercept = new Intercept(); //封装拦截信息
                intercept.setIntercept_ip(ipAddr);
                intercept.setIntercept_address(address);
                intercept.setIntercept_browser(browserName);
                intercept.setIntercept_os(osName);
                intercept.setIntercept_time(dateTime);
                intercept.setDesc("用户ip在黑名单");
                interceptService.insertIntercept(intercept);
                redisTemplate.opsForValue().set(key+ipAddr,"996",60*15, TimeUnit.SECONDS);
            }

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
