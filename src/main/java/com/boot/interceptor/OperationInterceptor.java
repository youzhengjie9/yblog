package com.boot.interceptor;

import com.boot.annotation.Operation;
import com.boot.pojo.OperationLog;
import com.boot.service.OperationService;
import com.boot.utils.IpToAddressUtil;
import com.boot.utils.SpringSecurityUtil;
import com.boot.utils.BrowserOS;
import com.boot.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * @author 游政杰
 */
@Component
public class OperationInterceptor implements HandlerInterceptor {

    @Autowired
    private OperationService operationService;

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @Autowired
    private HttpSession session;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        HandlerMethod handlerMethod= (HandlerMethod)handler;
        String name = handlerMethod.getMethod().getName(); //获取方法名
        Class<?> aClass = handlerMethod.getBean().getClass(); //获取类

        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

        Class<?>[] parameterType = new Class<?>[methodParameters.length]; //封装参数列表的所有类型
        for (int i = 0; i < methodParameters.length; i++) {
            parameterType[i]=methodParameters[i].getParameterType();
        }

        //获取注解

        Method method = aClass.getMethod(name, parameterType);
//        String desc = method.getAnnotation(Operation.class).value(); //获取注解值
        Operation annotation = method.getAnnotation(Operation.class);
        if(annotation!=null){ //防止方法上没有注解而报空指针异常
            String ipAddr = IpUtils.getIpAddr(request);
            OperationLog operationLog = new OperationLog();
            operationLog.setUsername(springSecurityUtil.currentUser(session));
            operationLog.setIp(ipAddr);
            operationLog.setAddress(IpToAddressUtil.getCityInfo(ipAddr));
            operationLog.setBrowser(BrowserOS.getBrowserName(request));
            operationLog.setOs(BrowserOS.getOsName(request));
            operationLog.setUri(requestURI);
            operationLog.setType(annotation.value());
            java.util.Date d=new java.util.Date();
            java.sql.Date date=new Date(d.getTime());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = simpleDateFormat.format(date);
            operationLog.setTime(dateTime);
            operationService.insertOperationLog(operationLog);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
}
