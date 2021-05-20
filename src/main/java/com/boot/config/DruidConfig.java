package com.boot.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
        servletServletRegistrationBean.addUrlMappings("/druid/**");
        HashMap<String, String> initParamter = new HashMap<>();
        initParamter.put("loginUsername","admin");
        initParamter.put("loginPassword","123456");
        servletServletRegistrationBean.setInitParameters(initParamter);
        return servletServletRegistrationBean;
    }






}
