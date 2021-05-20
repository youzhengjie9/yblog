package com.boot.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Servlet;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class DruidConfig {


    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource(){

        return new DruidDataSource();
    }


    /**
     *  public ServletRegistrationBean(T servlet, String... urlMappings)
     *
     *  
     *  public abstract class ResourceServlet extends HttpServlet {
     *     private static final Log LOG = LogFactory.getLog(ResourceServlet.class);
     *     public static final String SESSION_USER_KEY = "druid-user";
     *     public static final String PARAM_NAME_USERNAME = "loginUsername";
     *     public static final String PARAM_NAME_PASSWORD = "loginPassword";
     *     public static final String PARAM_NAME_ALLOW = "allow";
     *     public static final String PARAM_NAME_DENY = "deny";
     *     public static final String PARAM_REMOTE_ADDR = "remoteAddress";
     *  @return Bean
     */
    @Bean
    public ServletRegistrationBean druidStatBean(){
        ServletRegistrationBean servletServletRegistrationBean = new ServletRegistrationBean(new StatViewServlet());
        servletServletRegistrationBean.addUrlMappings("/druid/*");
//        servletServletRegistrationBean.addInitParameter("loginUsername","admin");
//        servletServletRegistrationBean.addInitParameter("loginPassword","123456");
        return servletServletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean() ;
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*"); // 所有请求进行监控处理
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.css,/druid/*");
        return filterRegistrationBean ;
    }




}
