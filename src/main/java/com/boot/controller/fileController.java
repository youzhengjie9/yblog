package com.boot.controller;

import com.boot.pojo.userDetail;
import com.boot.service.userDetailService;
import com.boot.utils.Commons;
import com.boot.utils.SpringSecurityUtil;
import com.boot.utils.bootstrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(path = "/file")
public class fileController {

    /**
     * 文件附件上传
     */
    @Autowired
    private SpringSecurityUtil springSecurityUtil;

    @Autowired
    private userDetailService userDetailService;

    @RequestMapping(path = "/list")
    public String toFileList(HttpSession session, Model model){


        String username = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail",userDetail);
        model.addAttribute("bootstrap",new bootstrap());
        model.addAttribute("commons", Commons.getInstance());
        return "back/file_list";
    }


    @RequestMapping(path = "/fileupload")
    public String fileupload(MultipartFile[] files,HttpSession session,Model model){

        if(files.length>0){
            System.out.println(files.length);

            for (MultipartFile file : files) {
                if(!file.isEmpty()){
                    System.out.println("========上传");
                    System.out.println(file.getOriginalFilename()+"===="+file.getSize());

                }
            }

        }



        String username = springSecurityUtil.currentUser(session);
        userDetail userDetail = userDetailService.selectUserDetailByUserName(username);
        model.addAttribute("userDetail",userDetail);
        model.addAttribute("bootstrap",new bootstrap());
        model.addAttribute("commons", Commons.getInstance());
        return "back/file_list";
    }



}
