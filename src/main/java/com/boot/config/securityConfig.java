package com.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

@Configuration
public class securityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;


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
                .loginPage("/login")
                .failureForwardUrl("/LoginfailPage")
                .and()
                //关闭csrf
//                .csrf().disable()
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
                .antMatchers("/druid/*").permitAll()
                .antMatchers("/user/**","/email/**","/plugins/**","/user_img/**","/article_img/**", "/assets/**", "/back/**", "/user/**")
                .permitAll()
                .antMatchers("/admin/**").hasRole("admin")
                .anyRequest().permitAll();
    }
}
