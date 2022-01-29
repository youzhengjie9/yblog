package com.boot.config;

import com.boot.locale.MyLocaleResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

@Configuration
public class LocaleBean {


    @Bean
    public LocaleResolver localeResolver(){
        return new MyLocaleResolver();
    }

}
