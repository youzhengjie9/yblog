package com.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class securityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;


    /**
     *  private String usersByUsernameQuery = "select username,password,enabled from users where username = ?";
     *  private String authoritiesByUsernameQuery = "select username,authority from authorities where username = ?";
     *
     * public JdbcUserDetailsManagerConfigurer<B> usersByUsernameQuery(String query) {
     * 		getUserDetailsService().setUsersByUsernameQuery(query);
     * 		return this;
     *        }
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

        String sql="select username,authority_id from t_user,t_user_authority where username = ? and t_user.id=t_user_authority.id";

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
                .antMatchers("/article_img/**","/assets/**","/back/**","/user/**")
                .permitAll()
                .anyRequest().authenticated();
    }
}
