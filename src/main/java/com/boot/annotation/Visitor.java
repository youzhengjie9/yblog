package com.boot.annotation;


import java.lang.annotation.*;

/**
 * @author 游政杰
 */
@Target({ElementType.METHOD}) //只允许加到方法上
@Retention(RetentionPolicy.RUNTIME) //运行时有效
@Documented
public @interface Visitor {
    /**
     * 自定义注解，作用是给有此注解的方法当有人访问时会进行记录
     */
    String desc(); //接口描述


}
