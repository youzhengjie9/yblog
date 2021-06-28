package com.boot.controller.pearAdmin;

import com.alibaba.fastjson.JSON;
import com.boot.data.ResponseData.layuiData;
import com.boot.pojo.blacklist;
import com.boot.service.blacklistService;
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
public class blackListController {

    @Autowired
    private blacklistService blacklistService;

    @ResponseBody
    @RequestMapping(path = "/blackData")
    public String blackData(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "limit", defaultValue = "10")int limit){

        layuiData<blacklist> blacklistlayuiData = new layuiData<>();

        PageHelper.startPage(page,limit);
        List<blacklist> blacklists = blacklistService.selectBlackList();

        int count = blacklistService.selectBlackCount();

        blacklistlayuiData.setCode(0);
        blacklistlayuiData.setMsg("");
        blacklistlayuiData.setData(blacklists);
        blacklistlayuiData.setCount(count);

        return JSON.toJSONString(blacklistlayuiData);
    }

}
