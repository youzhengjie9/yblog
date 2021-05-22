package com.boot.service.impl;

import com.boot.service.emailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class emailServiceImpl implements emailService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JavaMailSender mailSender;




    //生成验证码算法
    public static final String getCode(){
        String source = UUID.randomUUID().toString().replaceAll("-", "");
        Random random = new Random();
        int i = random.nextInt(10)+2;
        String res = source.substring(i, i + 6);
        return res;
    }


    @Override
    public void sendEmailCode(String email) {

        String code = getCode();

        //验证码5分钟失效
        redisTemplate.opsForValue().set("code_"+email,code,60*5, TimeUnit.SECONDS);

        //发送邮件




    }



}
