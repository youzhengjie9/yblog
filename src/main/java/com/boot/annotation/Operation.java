package com.boot.annotation;


import java.lang.annotation.*;

/**
 * @author 游政杰
 * @Date 2021/7/9 19:10
 */
@Target(ElementType.METHOD) //作用到方法上
@Retention(RetentionPolicy.RUNTIME) //运行时有效
@Documented
public @interface Operation {
    /**
     * 自定义注解：作用是标记指定的接口，当访问这个接口就会被拦截，然后把操作记录（也就是注解的值）放入数据库存储
     */
    String value() default "";

}
