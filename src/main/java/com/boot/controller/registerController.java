package com.boot.controller;

import com.boot.data.ResponseData.ArticleResponseData;
import com.boot.pojo.user;
import com.boot.pojo.user_authority;
import com.boot.service.emailService;
import com.boot.service.userService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import java.sql.Date;

@Controller
public class registerController {

    @Autowired
    private emailService emailService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private userService userService;
    @RequestMapping(path = "/toregister")
    public String toRegister() {

        return "comm/register";
    }


    @RequestMapping(path = "/register")
    public String register(user user,String code,Model model) {
        String email = user.getEmail();

        String redis_code = (String) redisTemplate.opsForValue().get("code_" + email); //从redis中获取验证码，然后和前端的比对


        if(code.equals(redis_code)){
            //可以注册,但是用户名相同会注册失败
            try {
                //注册代码
                Date date = new Date(new java.util.Date().getTime());
                BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
                //进行BCryptPasswordEncoder加密
                String encode_password = bCryptPasswordEncoder.encode(user.getPassword());
                user.setPassword(encode_password);
                user.setDate(date);
                user.setValid(1);
                userService.addUser(user);
                System.out.println("主键user："+user.getId());
                user_authority user_authority = new user_authority();
                user_authority.setUser_id(user.getId());
                user_authority.setAuthority_id(2);
                userService.addUserAuthority(user_authority);
                model.addAttribute("registerSuccess",1);
                return "comm/login";
            }catch (Exception e){
                e.printStackTrace();
                model.addAttribute("register_error",1);
                return "comm/register";
            }


        }else {
            model.addAttribute("register_error",1);
            return "comm/register";
        }


    }

    //跳转到发送验证码界面
    @GetMapping(path = "/toSendEmailPage")
    public String toEmailSendPage() {

        return "comm/emailSend";
    }

    //消息消费者
    @RabbitListener(bindings = {@QueueBinding(value = @Queue,
            exchange = @Exchange(name = "mail_Exchange", type = "direct"),
            key = "mail_key")})
    public void sendEmailComsumer(String msg) {

        try {
            emailService.sendEmailCode(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }


    //发送验证码接口
    //消息发送者
    @RequestMapping(path = "/sendCode")
    public String sendCodeToEamil(String email, Model model) {



        String regex="[1-9]\\d{7,10}@qq\\.com"; //qq邮箱正则表达式

        if(email==null||email.equals("")||!email.matches(regex))
        {
            model.addAttribute("error",1);
            return "comm/emailSend";
        }else {
            /**
             * void convertAndSend(String exchange, String routingKey, Object message) throws AmqpException;
             */

            //发送邮件消息，异步去处理耗时操作
            rabbitTemplate.convertAndSend("mail_Exchange","mail_key",email);

            //通过传值的方法，显示下一步按钮和提示信息,并且隐藏发送验证码的文本框
            model.addAttribute("canNext", 1);
            return "comm/emailSend";
        }


    }


    //下一步
    @GetMapping(path = "/next")
    public String sendEmailNext() {


        return "comm/register";
    }


}


