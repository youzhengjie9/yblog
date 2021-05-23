package com.boot.service.impl;

import com.boot.service.emailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class emailServiceImpl implements emailService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;//模板引擎

    @Value("${spring.mail.username}")
    private String fromEmail;


    //生成验证码算法
    public static final String getCode(){
        String source = UUID.randomUUID().toString().replaceAll("-", "");
        Random random = new Random();
        int i = random.nextInt(10)+2;
        String res = source.substring(i, i + 6);
        return res;
    }


    @Override
    public void sendEmailCode(String toEmail) throws MessagingException {

        String code = getCode(); //生成验证码
        //验证码5分钟失效
        redisTemplate.opsForValue().set("code_"+toEmail,code,60*5, TimeUnit.SECONDS);
        //发送模板邮件
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        Context context = new Context();
        context.setVariable("code",code);
        String process = templateEngine.process("comm/emailTemplate", context);

        mimeMessageHelper.setFrom(fromEmail);
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setSubject("blog验证码");
        mimeMessageHelper.setText(process,true);

        mailSender.send(mimeMessage);
    }



}
