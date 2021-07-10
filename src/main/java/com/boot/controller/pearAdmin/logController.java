package com.boot.controller.pearAdmin;

import com.alibaba.fastjson.JSON;
import com.boot.annotation.Operation;
import com.boot.data.ResponseData.layuiData;
import com.boot.pojo.loginLog;
import com.boot.service.LoginLogService;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author 游政杰
 */
@Controller
@RequestMapping(path = "/pear")
@Api("日志控制器")
public class logController {

    @Autowired
    private LoginLogService loginLogService;

    @Operation("进入登录日志界面")
    @ResponseBody
    @RequestMapping(path = "/log/loginlog")
    public String loginLogData(@RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "limit", defaultValue = "10") int limit){
        layuiData<loginLog> data = new layuiData<>();

        PageHelper.startPage(page,limit);
        List<loginLog> loginLogs = loginLogService.selectLoginLogAll();

        int count = loginLogService.loginLogCount();

        data.setCode(0);
        data.setMsg("");
        data.setData(loginLogs);
        data.setCount(count);
        return JSON.toJSONString(data);
    }



}
