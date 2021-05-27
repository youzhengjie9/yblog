package com.boot.comsumer;

import com.boot.service.emailService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
public class rabbitmqComsumer {

    @Autowired
    private emailService emailService;
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



}
