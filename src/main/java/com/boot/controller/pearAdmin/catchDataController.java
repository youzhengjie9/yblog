package com.boot.controller.pearAdmin;

import com.boot.annotation.Visitor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * @author 游政杰
 * @Date 2021/6/30
 */
@Controller("pearCatchDataController")
@RequestMapping(path = "/pear")
@CrossOrigin
public class catchDataController {




    //爬取数据
    @Visitor(desc = "爬取数据")
    @RequestMapping(path = "/toCatchData")
    public String toCatchData() {


        return "back/newback/article/catch_list";
    }

}
