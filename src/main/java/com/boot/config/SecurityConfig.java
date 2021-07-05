package com.boot.config;

import com.boot.constant.themeConstant;
import com.boot.filter.VerifyCodeFilter;
import com.boot.pojo.setting;
import com.boot.service.settingService;
import com.boot.service.userDetailService;
import com.boot.utils.SpringSecurityUtil;
import com.boot.utils.ipUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 游政杰
 */
@Configuration
@EnableWebSecurity //开启SpringSecurity的功能
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启注解控制权限
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    private RedisTemplate redisTemplate;

    private final Logger logger = Logger.getLogger(SecurityConfig.class);

    @Autowired
    private settingService settingService;

    @Autowired
    private VerifyCodeFilter verifyCodeFilter;

    @Autowired
    private UserDetailsService userDetailsService;

//    private final String REMEMBER_KEY = "REMEMBER_"; //记住我的key前缀


    /**
     * ===========第一次使用把下面的代码解除注释，使用完之后再注释回去
     * @return
     */
    //自动生成记住我需要的表
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        // 配置数据源
        jdbcTokenRepository.setDataSource(dataSource);
        // 第一次启动的时候自动建表（可以不用这句话，自己手动建表，源码中有语句的）
        // jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }


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
        //配置过滤器
        http.addFilterBefore(verifyCodeFilter, UsernamePasswordAuthenticationFilter.class);
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

                        //登入成功之后要把登入验证码的缓存标记给删除掉
                        redisTemplate.delete(ipAddr + "_lg");

                        UsernamePasswordAuthenticationToken s = (UsernamePasswordAuthenticationToken) authentication;

                        String name = s.getName(); //获取登录用户名

                        themeConstant.curTheme = settingService.selectUserSetting(name).getTheme(); //查询用户主题

                        logger.debug("ip地址：" + ipAddr + "登录成功");

//                        //使用cookie+Redis实现记住我功能
//                        String rememberme = request.getParameter("remember-me");
//                        if (rememberme != null && rememberme.equals("on")) { //此时激活记住我
//
//                            setRememberme(name, request, httpServletResponse); //记住我实现
//
//
//                        }


                        //这里不要用转发，不然会有一些bug
//                        request.getRequestDispatcher("/").forward(request,httpServletResponse);
                        httpServletResponse.sendRedirect("/");
                    }
                })
                .failureForwardUrl("/LoginfailPage")
                .and()
                .rememberMe()   //对记住我进行设置
                .tokenRepository(persistentTokenRepository())   // 设置TokenRepository
                .tokenValiditySeconds((int) System.currentTimeMillis()+60*10*1000)  //设置Token的有效时间
                .userDetailsService(userDetailsService)    //使用userDetailsService用Token从数据库中获取用户自动登录
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
                //这句代码一定要加，为了防止spring过滤静态资源
                .antMatchers("/user/**", "/email/**", "/plugins/**", "/user_img/**", "/article_img/**", "/assets/**", "/back/**",
                        "/user/**", "/pear-admin/**", "/component/**", "/static/**", "/pear/captcha"
                        , "/config/**", "/favicon.ico")
                .permitAll()
                .antMatchers("/admin/**", "/monitor/**", "/usermanager/**",
                        "/article/updateAllowComment",
                        "/link/**", "/visitor/**", "/chart/**", "/black/**").hasRole("admin")
                .antMatchers("/myuser/**", "/img/**", "/catchArticle/**", "/like/**").hasAnyRole("admin", "common")
                .antMatchers("/sliderCaptcha/**").permitAll()
                .anyRequest().permitAll()
                .and()
//                如果不加这段代码，iframe嵌入的Druid监控界面会出现（使用 X-Frame-Options 拒绝网页被 Frame 嵌入）
                .headers()
                .frameOptions()
                .disable();
    }

//    private void setRememberme(String name, HttpServletRequest request, HttpServletResponse response) {
//
//        //通过redis和cookie的token和name的一致性去判断
//        String token = UUID.randomUUID().toString().replace("-", "");
//
//        Cookie cookie = new Cookie(REMEMBER_KEY + name, token);
//        cookie.setMaxAge(60 * 15); //15分钟记住我
//
//        redisTemplate.opsForValue().set(REMEMBER_KEY + name, token, 60 * 15, TimeUnit.SECONDS);
//
//        response.addCookie(cookie);
//
//    }
}