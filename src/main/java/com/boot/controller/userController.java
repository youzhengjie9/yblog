package com.boot.controller;


import com.alibaba.fastjson.JSON;
import com.boot.data.ResponseData.ArticleResponseData;
import com.boot.pojo.user;
import com.boot.pojo.userDetail;
import com.boot.service.userDetailService;
import com.boot.service.userService;
import com.boot.utils.Commons;
import com.boot.utils.SpringSecurityUtil;
import com.boot.utils.bootstrap;
import com.boot.utils.fileUtil;
import io.swagger.annotations.Api;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * @author 游政杰
 * 2021/5/28
 */
@Controller
@RequestMapping(path = "/myuser")
@Api("个人资料控制器")
public class userController {

    @Autowired
    private userDetailService userDetailService;

    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @Autowired
    private userService userService;

    @RequestMapping(path = "/list")
//    @ResponseBody
    public String toUserList(HttpSession session, Model model){



        String name = springSecurityUtil.currentUser(session);

        user user = userService.selectUserInfoByuserName(name);


        model.addAttribute("user",user);
        model.addAttribute("curName",name);
        model.addAttribute("commons", Commons.getInstance());
        model.addAttribute("bootstrap",new bootstrap());

        userDetail userDetail = userDetailService.selectUserDetailByUserName(name);
        model.addAttribute("userDetail",userDetail);

        return "back/user_list";
    }


    //上传头像
    @RequestMapping(path = "/uploadIcon")
//    @ResponseBody
    public String uploadIcon(HttpSession session,userDetail userDetail,String email, Model model, MultipartFile file, HttpServletRequest request) throws IOException {

        boolean flag = file.isEmpty(); //判断是否是空
        String name = springSecurityUtil.currentUser(session);
        if(!flag){ //如果不是空的,说明传入了头像
            InputStream inputStream = file.getInputStream(); //获取文件流
            byte fileByteArray[] =new byte[inputStream.available()];
            inputStream.read(fileByteArray);//读取到一个字节数组中
            String userImagePath = fileUtil.writeImage(file.getOriginalFilename(),fileByteArray); //传入头像名

            userDetail.setIcon(userImagePath); //把头像路径设置进去

            //删除之前的头像
            com.boot.pojo.userDetail userDetail1 = userDetailService.selectUserDetailByUserName(name);

            String icon = userDetail1.getIcon();
            String iconPath=fileUtil.getStaticPath()+icon; //拼接头像地址
            File file1 = new File(iconPath);
            try {
                if (file1.exists()){
                    file1.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        userDetailService.updateUserDetail(userDetail); //修改之后再查询一次头像地址

        com.boot.pojo.userDetail userDetail1 = userDetailService.selectUserDetailByUserName(name);
        model.addAttribute("userDetail",userDetail1);

        if(email!=null && !email.equals("")){
            userService.updateEmail(userDetail.getName(),email);
        }



        user user = userService.selectUserInfoByuserName(name);



        model.addAttribute("user",user);
        model.addAttribute("curName",name);
        model.addAttribute("bootstrap",new bootstrap());
        model.addAttribute("commons",Commons.getInstance());



        return "back/user_list";

    }

    @RequestMapping(path = "/updatePassword")
    public String updatePassword(String oldPassword,String newPassword1
                                 ,String newPassword2,
                                 HttpSession session,Model model){

        boolean mt= (newPassword1.equals(newPassword2))?true:false;

        String name = springSecurityUtil.currentUser(session);
        if(mt){
            //创建BCrypt加密器
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String password = userService.selectPasswordByuserName(name); //获取数据库已加密的密码
            boolean matches = passwordEncoder.matches(oldPassword, password); //进行密码校对


            if(matches){ //如果匹配成功


                //进行修改
                String encodePassword = passwordEncoder.encode(newPassword1); //放入数据库
                userService.updatePassword(name,encodePassword);

            }

        }

        user user = userService.selectUserInfoByuserName(name);
        model.addAttribute("user",user);
        model.addAttribute("curName",name);
        model.addAttribute("bootstrap",new bootstrap());
        model.addAttribute("commons",Commons.getInstance());
        userDetail userDetail = userDetailService.selectUserDetailByUserName(name);
        model.addAttribute("userDetail",userDetail);

        return "back/user_list";
    }






}
