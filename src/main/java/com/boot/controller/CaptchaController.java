package com.boot.controller;

import com.alibaba.fastjson.JSON;
import com.boot.data.ResponseData.ResponseJSON;
import com.boot.utils.IpUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;



@Controller
@RequestMapping(path = "/sliderCaptcha")
@Api("滑块验证码校验接口")
public class CaptchaController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(path = "/checkSuccess")
    @ResponseBody
    public String checkSuccess(int[] datas, HttpServletRequest request) {
        String s = null;
        //可能会存在精度差异，校验
        try {
            int sum = 0;
            for (int data : datas) {
                sum += data;
            }
            double avg = sum * 1.0 / datas.length;

            double sum2 = 0.0;

            for (int data : datas) {
                sum2 += Math.pow(data - avg, 2);
            }


            double stddev = sum2 / datas.length;

            ResponseJSON responseJSON = new ResponseJSON();
            boolean bl = stddev != 0; //判断是否校验成功
            if (bl) { //如果是成功的，就把当前IP放入缓存，并设置一定时间过期，不过在登入成功后要删除缓存，实现”验证一次登入一次“
                String ipAddr = IpUtils.getIpAddr(request);
                redisTemplate.opsForValue().set(ipAddr + "_lg", 1,30, TimeUnit.SECONDS);
            }
            responseJSON.setDt(bl);
            s = JSON.toJSONString(responseJSON);
            return s;

        } catch (Exception e) {

        }
        return s;

    }
}
