package com.boot.controller.pearAdmin;

import com.boot.annotation.Operation;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 游政杰
 */
@Controller("PearMonitorController")
@RequestMapping(path = "/pear")
@Api("监控控制器")
public class monitorController {


    @Operation("进入接口监控界面")
    @RequestMapping(path = "/monitorInterface")
    public String toMonitorInterface(){

        return "back/newback/article/monitor_interface";
    }




}
