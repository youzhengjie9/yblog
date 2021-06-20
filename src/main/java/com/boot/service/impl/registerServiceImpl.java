package com.boot.service.impl;

import com.boot.constant.themeConstant;
import com.boot.pojo.setting;
import com.boot.pojo.user;
import com.boot.pojo.user_authority;
import com.boot.service.registerService;
import com.boot.service.settingService;
import com.boot.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

@Service
@Transactional
public class registerServiceImpl implements registerService {

    @Autowired
    private userService userService;

    @Autowired
    private settingService settingService;

    @Override
    public void register(user user) {
        try {
            //注册代码
            Date date = new Date(new java.util.Date().getTime());
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            //进行BCryptPasswordEncoder加密
            String encode_password = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(encode_password);
            user.setDate(date);
            user.setValid(1);
            userService.addUser(user);
//        System.out.println("主键user："+user.getId());
            user_authority user_authority = new user_authority();
            user_authority.setUser_id(user.getId());
            user_authority.setAuthority_id(2);
            userService.addUserAuthority(user_authority);
            //添加用户默认设置
            setting setting = new setting();
            setting.setName(user.getUsername());
            setting.setTheme(themeConstant.CALM_THEME);
            setting.setFoot("----2021----");
            setting.setLogo("/user/img/bloglogo.jpg");
            settingService.addSettingByUser(setting);
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }
}
