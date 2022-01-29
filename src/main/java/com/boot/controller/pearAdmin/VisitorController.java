package com.boot.controller.pearAdmin;

import com.alibaba.fastjson.JSON;
import com.boot.data.ResponseData.LayuiData;
import com.boot.pojo.Visitor;
import com.boot.service.VisitorService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
/**
 * @author 游政杰
 *
 */
@Controller("pearVisitorController")
@RequestMapping(path = "/pear")
@CrossOrigin
public class VisitorController {

    @Autowired
    private VisitorService visitorService;


    @ResponseBody
    @RequestMapping(path = "/visitorData")
    public String visitorData(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "limit", defaultValue = "10") int limit){


        LayuiData<Visitor> visitorlayuiData = new LayuiData<>();

        PageHelper.startPage(page,limit);
        List<Visitor> visitors = visitorService.selectVisitor();

        int count = visitorService.selectVistorCount();
        visitorlayuiData.setCode(0);
        visitorlayuiData.setCount(count);
        visitorlayuiData.setMsg("");
        visitorlayuiData.setData(visitors);

        return JSON.toJSONString(visitorlayuiData);
    }


}
