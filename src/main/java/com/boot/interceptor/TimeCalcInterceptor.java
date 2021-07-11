package com.boot.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 游政杰
 */
@Component
public class TimeCalcInterceptor implements HandlerInterceptor {

    /**
     * 监控每个接口的耗时
     */

    //进入接口计时开始
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("startTime",System.currentTimeMillis());
        return true;
    }


    //完成之后计时结束
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long endTime = System.currentTimeMillis();

        long startTime = (long) request.getAttribute("startTime");

        long res=endTime-startTime;

        String requestURI = request.getRequestURI();

        System.out.println("uri:"+requestURI+"===>接口耗时："+res+"ms");


    }
}
