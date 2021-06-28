package com.boot.filter.config;

import com.boot.filter.ipFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class filterConfig {

    /**
     * 注意：==============此处有坑====
     */
    @Autowired
    private ipFilter ipFilter; //自定义的filter一定要通过这种方式为下面注册filterBean，不然这个ipfilter里面将注入不了


    @Bean
    public FilterRegistrationBean filter1() {

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        /**
         * 坑出：因为下面的setFilter我之前是写成setFilter(new ipFilter()),忽略了我已经在ipFilter加入Component
         * 注解，Spring管理的东西是不能通过new来用的，否则Spring会管理不了，所以我们要采取注入的方法注入ipFilter类
         * ，来替代new的方式获取ipfilter的对象
         */
        filterRegistrationBean.setFilter(ipFilter); //添加自定义过滤器类
        filterRegistrationBean.addUrlPatterns("/*"); //过滤路径
        filterRegistrationBean.addInitParameter("excludedUris","/err/black,/assets/**,/user/**,/back/**,/static/**");//需要排除的uri
        return filterRegistrationBean;
    }


}
