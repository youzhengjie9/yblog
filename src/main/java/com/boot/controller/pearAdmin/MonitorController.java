package com.boot.controller.pearAdmin;

import com.alibaba.fastjson.JSON;
import com.boot.annotation.Operation;
import com.boot.data.ResponseData.LayuiData;
import com.boot.pojo.TimeCalc;
import com.boot.service.TimeCalcService;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author 游政杰
 */
@Controller("PearMonitorController")
@RequestMapping(path = "/pear")
@Api("监控控制器")
public class MonitorController {

    @Autowired
    private TimeCalcService timeCalcService;

    @Operation("进入接口监控界面")
    @RequestMapping(path = "/monitorInterface")
    public String toMonitorInterface() {

        return "back/newback/article/monitor_interface";
    }

    @ResponseBody
    @RequestMapping(path = "/data/monitorInterface")
    public String monitorInterfaceData(@RequestParam(value = "page", defaultValue = "1") int page,
                                       @RequestParam(value = "limit", defaultValue = "10") int limit,
                                       @RequestParam(value = "uri", defaultValue = "") String uri) {

        if (StringUtils.isBlank(uri)) {

            LayuiData<TimeCalc> timeCalclayuiData = new LayuiData<>();
            PageHelper.startPage(page, limit);
            List<TimeCalc> timeCalcs = timeCalcService.selectAllTimeCalc();

            int count = timeCalcService.selectAllCount();

            timeCalclayuiData.setMsg("");
            timeCalclayuiData.setCode(0);
            timeCalclayuiData.setData(timeCalcs);
            timeCalclayuiData.setCount(count);

            return JSON.toJSONString(timeCalclayuiData);

        } else {


            LayuiData<TimeCalc> timeCalclayuiData = new LayuiData<>();

            PageHelper.startPage(page, limit);
            List<TimeCalc> timeCalcs = timeCalcService.selectAllTimeCalcByUri(uri);
            int count = timeCalcService.selectCountByUri(uri);

            timeCalclayuiData.setMsg("");
            timeCalclayuiData.setCode(0);
            timeCalclayuiData.setCount(count);
            timeCalclayuiData.setData(timeCalcs);

            return JSON.toJSONString(timeCalclayuiData);
        }

    }


}
