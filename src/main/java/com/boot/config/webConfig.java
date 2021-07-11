package com.boot.config;

import com.boot.interceptor.OperationInterceptor;
import com.boot.interceptor.RememberInterceptor;
import com.boot.interceptor.TimeCalcInterceptor;
import com.boot.interceptor.visitorInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 游政杰
 */
@Configuration
public class webConfig implements WebMvcConfigurer {

    @Autowired
    private visitorInterceptor visitorInterceptor;

    @Autowired
    private RememberInterceptor rememberInterceptor;

    @Autowired
    private OperationInterceptor operationInterceptor;

    @Autowired
    private TimeCalcInterceptor timeCalcInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(visitorInterceptor)
                .addPathPatterns("/admin/**","/archive/list","/black/list","/catchArticle/list","/chart/list")
                .addPathPatterns("/","/page/**","/img/list","/link/list","/usermanager/list")
                .addPathPatterns("/article/**","/visitor/list","/myuser/list","/search/**")
                .excludePathPatterns("/admin/modify","/admin/publish","/admin/deleteArticle"
                        ,"/admin/updateCategory","/admin/deleteCategory","/admin/addCategory");

        registry.addInterceptor(rememberInterceptor)
                .addPathPatterns("/","/article/**","/archive/list","/search/**","/page/**");


        registry.addInterceptor(operationInterceptor)
                .addPathPatterns("/pear/**")
                //排除静态资源和提供数据的接口
                .excludePathPatterns("/static/**","/pear/userInfoData","/pear/articledata","/pear/categoryData"
                ,"/pear/tagsData","/pear/captcha","/pear/userManagerData","/pear/linkData","/pear/visitorData"
                ,"/pear/blackData","/pear/interceptData","/pear/log/loginlog");

        registry.addInterceptor(timeCalcInterceptor)
                //拦截所有接口
                .addPathPatterns("/**")
                //只需排除一下静态资源即可
                .excludePathPatterns("/user/**","/assets/**","/user_img/**"
                ,"/article_img/**","/back/**","/big_img/**","/component/**","/config/**"
                ,"/diagram-viewer/**","/email/**","/pear-admin/**","/plugins/**","/small_img/**"
                ,"/favicon.ico","/error");
    }
}
