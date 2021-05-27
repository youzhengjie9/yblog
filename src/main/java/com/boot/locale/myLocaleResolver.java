package com.boot.locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Configuration
public class myLocaleResolver implements org.springframework.web.servlet.LocaleResolver {
    /**
     * 国际化
     * @param httpServletRequest
     * @return
     */

    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        String l = httpServletRequest.getParameter("l");
        Locale defaultLocale = Locale.getDefault();
        if(!StringUtils.isEmpty(l)){
            String[] s = l.split("_");
            defaultLocale=new Locale(s[0],s[1]);
            return  defaultLocale;
        }else {
            return defaultLocale;
        }
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}
