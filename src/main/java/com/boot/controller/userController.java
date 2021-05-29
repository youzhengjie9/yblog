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
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@RequestMapping(path = "/myuser")
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


        return "back/user_list";
    }


    //上传头像
    @RequestMapping(path = "/uploadIcon")
//    @ResponseBody
    public String uploadIcon(userDetail userDetail,String email, Model model, MultipartFile file, HttpServletRequest request) throws IOException {

        boolean flag = file.isEmpty(); //判断是否是空
        if(!flag){ //如果不是空的
            InputStream inputStream = file.getInputStream(); //获取文件流
            byte fileByteArray[] =new byte[inputStream.available()];
            inputStream.read(fileByteArray);//读取到一个字节数组中
            String userImagePath = fileUtil.writeImage(file.getOriginalFilename(),fileByteArray); //传入头像名
            userDetail.setIcon(userImagePath);
        }

        userDetailService.updateUserDetail(userDetail);

        if(email!=null && !email.equals("")){
            userService.updateEmail(email);
        }

        model.addAttribute("bootstrap",new bootstrap());
        model.addAttribute("commons",Commons.getInstance());

        return "back/user_list";

    }








}
