package com.boot.interceptor;

import com.boot.annotation.Operation;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author 游政杰
 */
@Component
public class OperationInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        System.out.println(requestURI);
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
        String value = method.getAnnotation(Operation.class).value(); //获取注解值




        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
}
