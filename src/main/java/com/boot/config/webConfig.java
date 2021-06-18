package com.boot.config;

import com.boot.interceptor.visitorInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class webConfig implements WebMvcConfigurer {

    @Autowired
    private visitorInterceptor visitorInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(visitorInterceptor)
                .addPathPatterns("/admin/**","/archive/list","/black/list","/catchArticle/list","/chart/list")
                .addPathPatterns("/").addPathPatterns("/page/**")
                .addPathPatterns("/article/**")
                .excludePathPatterns("/admin/modify","/admin/publish","/admin/deleteArticle"
                        ,"/admin/updateCategory","/admin/deleteCategory","/admin/addCategory"
                );
    }
}
