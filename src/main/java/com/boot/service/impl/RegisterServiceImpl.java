package com.boot.service.impl;

import com.boot.constant.ThemeConstant;
import com.boot.pojo.Setting;
import com.boot.pojo.User;
import com.boot.pojo.UserDetail;
import com.boot.pojo.UserAuthority;
import com.boot.service.RegisterService;
import com.boot.service.SettingService;
import com.boot.service.UserDetailService;
import com.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

@Service
@Transactional
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserService userService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private UserDetailService userDetailService;

    @Override
    public void register(User user) {
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


            UserAuthority user_authority = new UserAuthority();
            user_authority.setUser_id(user.getId());
            user_authority.setAuthority_id(2);
            userService.addUserAuthority(user_authority);


            //设置userDetail
            UserDetail userDetail = new UserDetail();
            userDetail.setName(user.getUsername());
            userDetailService.addUserDetail(userDetail);

            //添加用户默认设置
            Setting setting = new Setting();
            setting.setName(user.getUsername());
            setting.setTheme(ThemeConstant.CALM_THEME);
            setting.setFoot("----2021----");
            setting.setLogo("/user/img/bloglogo.jpg");
            settingService.addSettingByUser(setting);


        } catch (Exception e) {
            throw new RuntimeException();
        }

    }
}
