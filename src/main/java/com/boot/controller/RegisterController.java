package com.boot.controller;

import com.alibaba.fastjson.JSON;
import com.boot.data.ResponseData.LayuiJSON;
import com.boot.pojo.User;
import com.boot.service.EmailService;
import com.boot.service.RegisterService;
import com.boot.utils.IpUtils;
import io.swagger.annotations.Api;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author 游政杰
 * 2021/5/23 2:16
 */
@Controller
@Api("注册帐号控制器")
public class RegisterController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RegisterService registerService;


    @RequestMapping(path = "/toregister")
    public String toRegister(HttpServletRequest request,Model model) {
        String ip = IpUtils.getIpAddr(request);

//        redisTemplate.hasKey("")

        //获取按钮的倒计时(-2是不存在/已过期，)
        Long expire = redisTemplate.getExpire("codeButton_" + ip);

        model.addAttribute("expire",expire);


        return "comm/register";
    }


    @RequestMapping(path = "/register")
    @ResponseBody
    public String register(User user, String code, Model model) {


        LayuiJSON json = new LayuiJSON();

        String redis_code = (String) redisTemplate.opsForValue().get("code_" + user.getEmail()); //从redis中获取验证码，然后和前端的比对

        if (code.equals(redis_code)) {
            //可以注册,但是用户名相同会注册失败
            try {
                //注册代码
                registerService.register(user);
                json.setMsg("注册成功");
                json.setSuccess(true);
                redisTemplate.delete("code_"+user.getEmail());
                return JSON.toJSONString(json);
            } catch (Exception e) {
                e.printStackTrace();
                json.setMsg("注册失败");
                json.setSuccess(false);
                return JSON.toJSONString(json);
            }

        } else {
            json.setMsg("验证码不一致,注册失败");
            json.setSuccess(false);
            return JSON.toJSONString(json);
        }

    }


    //发送验证码接口
    //消息发送者
    @RequestMapping(path = "/sendCode")
    @ResponseBody
    public String sendCodeToEamil(String email, HttpServletRequest request, Model model) {

        LayuiJSON json = new LayuiJSON();

        try {
            //发送邮件消息，异步去处理耗时操作
            rabbitTemplate.convertAndSend("mail_Exchange", "mail_key", email);
            //获取ip
            String ip = IpUtils.getIpAddr(request);
            //发送验证码成功之后启动按钮倒计时，存储进缓存中  ，key格式(codeButton_+IP)
            redisTemplate.opsForValue().set("codeButton_" + ip, "验证码按钮倒计时", 60, TimeUnit.SECONDS);

            json.setSuccess(true);
            json.setMsg("发送验证码成功");
            return JSON.toJSONString(json);
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg("发送验证码失败");
            return JSON.toJSONString(json);
        }
    }


}


