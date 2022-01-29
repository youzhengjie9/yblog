package com.boot.controller.pearAdmin;

import com.alibaba.fastjson.JSON;
import com.boot.data.ResponseData.LayuiData;
import com.boot.pojo.LoginLog;
import com.boot.pojo.OperationLog;
import com.boot.service.LoginLogService;
import com.boot.service.OperationService;
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
public class LogController {

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private OperationService operationService;

    @ResponseBody
    @RequestMapping(path = "/log/loginlog")
    public String loginLogData(@RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "limit", defaultValue = "10") int limit){
        LayuiData<LoginLog> data = new LayuiData<>();

        PageHelper.startPage(page,limit);
        List<LoginLog> loginLogs = loginLogService.selectLoginLogAll();

        int count = loginLogService.loginLogCount();

        data.setCode(0);
        data.setMsg("");
        data.setData(loginLogs);
        data.setCount(count);
        return JSON.toJSONString(data);
    }

    @ResponseBody
    @RequestMapping(path = "/log/operationLog")
    public String operationLogData(@RequestParam(value = "page",defaultValue = "1") int page,
                                   @RequestParam(value = "limit",defaultValue = "10") int limit){

        LayuiData<OperationLog> data = new LayuiData<>();

        PageHelper.startPage(page, limit);
        List<OperationLog> operationLogs = operationService.selectAllOperationLog();

        int count = operationService.selectOperationCount();

        data.setCode(0);
        data.setMsg("");
        data.setData(operationLogs);
        data.setCount(count);

        return JSON.toJSONString(data);
    }




}
