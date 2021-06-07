package com.boot.config;

import com.boot.utils.ipUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class securityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    private final Logger logger = Logger.getLogger(securityConfig.class);


    /**
     * private String usersByUsernameQuery = "select username,password,enabled from users where username = ?";
     * private String authoritiesByUsernameQuery = "select username,authority from authorities where username = ?";
     * <p>
     * public JdbcUserDetailsManagerConfigurer<B> usersByUsernameQuery(String query) {
     * getUserDetailsService().setUsersByUsernameQuery(query);
     * return this;
     * }
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

//        String sql="select username,authority_id from t_user,t_user_authority where username = ? and t_user.id=t_user_authority.id";
        String sql = "select u.username,a.authority from t_user u,t_authority a," +
                "t_user_authority ua where ua.user_id=u.id " +
                "and ua.authority_id=a.id and u.username =?";
        auth.jdbcAuthentication().dataSource(dataSource)
                .passwordEncoder(encoder)
                .usersByUsernameQuery("select username,password,valid from t_user where username = ?")
                .authoritiesByUsernameQuery(sql);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
//                .loginPage("/login")
                .loginPage("/loginPage") //登录页接口
                .loginProcessingUrl("/login") //登录过程接口（也就是登录表单提交的接口）
                //登录成功处理
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        String ipAddr = ipUtils.getIpAddr(request);
                        System.out.println("=====================");
                        System.out.println("登录成功：访问者ip地址：" + ipAddr);
                        logger.debug("ip地址：" + ipAddr + "登录成功");
                        //这里不要用转发，不然会有一些bug
//                        request.getRequestDispatcher("/").forward(request,httpServletResponse);
                        httpServletResponse.sendRedirect("/");
                    }
                })
                .failureForwardUrl("/LoginfailPage")

                .and()
                //不写这段代码，druid监控sql将失效（原因未明）
                .csrf().ignoringAntMatchers("/druid/**")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/page/1")
                .and()
                .authorizeRequests()
                .antMatchers("/page/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/loginPage").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/article/**").permitAll()
                .antMatchers("/druid/**").permitAll()
                .antMatchers("/user/**", "/email/**", "/plugins/**", "/user_img/**", "/article_img/**", "/assets/**", "/back/**", "/user/**")
                .permitAll()
                .antMatchers("/admin/**", "/monitor/**", "/usermanager/**",
                        "/article/updateAllowComment",
                        "/link/**", "/visitor/**").hasRole("admin")
                .antMatchers("/myuser/**", "/img/**").hasAnyRole("admin", "common")
                .anyRequest().permitAll()
                .and()
//                如果不加这段代码，iframe嵌入的Druid监控界面会出现（使用 X-Frame-Options 拒绝网页被 Frame 嵌入）
                .headers()
                .frameOptions()
                .disable();
    }
}
