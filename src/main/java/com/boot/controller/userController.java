package com.boot.controller;


import com.alibaba.fastjson.JSON;
import com.boot.data.ResponseData.ArticleResponseData;
import com.boot.utils.Commons;
import com.boot.utils.bootstrap;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(path = "/user")
public class userController {

    @RequestMapping(path = "/list")
//    @ResponseBody
    public String toUserList(@RequestParam(value = "option",defaultValue = "1") int option, Model model){
        //通过option来判断显示基本资料还是密码修改
        //1就是基本资料  2就是密码修改
        //为了防止用户修改传入的option值，我们就采取option==2，密码修改，option!=2，基本资料

//        System.out.println(option);
//        if(option==2){
//            model.addAttribute("op",2);
//        }else {
//            model.addAttribute("op",1);
//        }


        model.addAttribute("commons", Commons.getInstance());
        model.addAttribute("bootstrap",new bootstrap());


        return "back/user_list";
    }


    //上传头像
    @RequestMapping(path = "/uploadIcon")
//    @ResponseBody
    public String uploadIcon(Model model,MultipartFile file,HttpServletRequest request) throws IOException {

//        boolean flag = file.isEmpty(); //判断是否是空
//        if(!flag){ //如果不是空的
//            InputStream inputStream = file.getInputStream(); //获取文件流
//            byte fileByteArray[] =new byte[inputStream.available()];
//            inputStream.read(fileByteArray);
//
//
//
//        }

        String path = ResourceUtils.getURL("classpath:static").getPath();
        System.out.println(path);


        model.addAttribute("bootstrap",new bootstrap());
        model.addAttribute("commons",Commons.getInstance());

        return "back/user_list";

    }








}
