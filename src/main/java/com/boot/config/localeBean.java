package com.boot.config;

import com.boot.locale.myLocaleResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

@Configuration
public class localeBean {


    @Bean
    public LocaleResolver localeResolver(){
        return new myLocaleResolver();
    }

}
