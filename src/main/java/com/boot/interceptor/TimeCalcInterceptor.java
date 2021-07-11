package com.boot.interceptor;

import com.boot.pojo.TimeCalc;
import com.boot.service.TimeCalcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 游政杰
 */
@Component
public class TimeCalcInterceptor implements HandlerInterceptor {


    @Autowired
    private TimeCalcService timeCalcService;

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

//        System.out.println("uri:"+requestURI+"===>接口耗时："+res+"ms");

        TimeCalc timeCalc = new TimeCalc();
        timeCalc.setUri(requestURI);
        timeCalc.setCalc(res+"ms");

        Date date = new Date();
        java.sql.Date date1=new java.sql.Date(date.getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        timeCalc.setTime(simpleDateFormat.format(date1));
        timeCalcService.insertTimeCalc(timeCalc);
    }
}
