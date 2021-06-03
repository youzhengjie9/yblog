package com.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class swaggerConfig {  //配置swagger2


    /**
     * Springfox提供Docket对象，为其设置相关属性，将其注册成为spring的bean后，
     * 可以在接口文档中展示（可配置多个Docket的bean，对应不同分组的接口）
     *
     * @return
     */

    @Bean
    public Docket getUserDocket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                //api标题
                .title("blog system")
                // api描述
                .description("blog system")
                //版本号
                .version("1.0.1")
                //本API负责人的联系信息
                .contact(new Contact("游政杰","baidu.com","1550324080@qq.com"))
                .build();
        return new Docket(DocumentationType.SWAGGER_2)//文档类型（swagger2）
                .apiInfo(apiInfo)//设置包含在json ResourceListing响应中的api元信息
                .select()//启动用于api选择的构建器
                .apis(RequestHandlerSelectors.basePackage("com.boot.controller"))//扫描接口的包
                .paths(PathSelectors.any())//路径过滤器（扫描所有路径）
                .build();
    }


}
