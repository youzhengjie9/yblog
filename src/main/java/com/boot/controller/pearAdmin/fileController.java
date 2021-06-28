package com.boot.controller.pearAdmin;

import com.boot.annotation.Visitor;
import com.boot.pojo.img;
import com.boot.service.imgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "/pear")
@CrossOrigin
public class fileController {

    @Autowired
    private imgService imgService;

    //附件管理
    @Visitor(desc = "附件管理")
    @RequestMapping(path = "/toFileUpload")
    public String toFileUpload(Model model) {

        List<img> imgs = imgService.selectAllImg();
        model.addAttribute("imgs",imgs);



        return "back/newback/article/img_list";
    }


}
