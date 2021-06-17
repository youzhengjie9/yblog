package com.boot.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD}) //只允许加到方法上
@Retention(RetentionPolicy.RUNTIME) //运行时有效
public @interface Visitor {
    /**
     * 自定义注解，作用是给有此注解的方法当有人访问时会进行记录
     */


}
