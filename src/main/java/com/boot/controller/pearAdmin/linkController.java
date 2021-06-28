package com.boot.controller.pearAdmin;

import com.alibaba.fastjson.JSON;
import com.boot.data.ResponseData.layuiData;
import com.boot.pojo.link;
import com.boot.service.linkService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
@Controller
@RequestMapping(path = "/pear")
@CrossOrigin
public class linkController {


    @Autowired
    private linkService linkService;

    /**
     *
     * @param page layui分页默认会传page和limit的值，所以要进行接收
     * @param limit
     * @return json
     */
    @ResponseBody
    @RequestMapping(path = "/linkData")
    public String linkData(@RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "limit", defaultValue = "6") int limit){


        layuiData<link> linklayuiData = new layuiData<>();
        PageHelper.startPage(page,limit);
        List<link> links = linkService.selectAllLink();
        int count = linkService.linkCount();
        linklayuiData.setCode(0);
        linklayuiData.setMsg("");
        linklayuiData.setCount(count);
        linklayuiData.setData(links);


        return JSON.toJSONString(linklayuiData);
    }


}
