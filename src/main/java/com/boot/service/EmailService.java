package com.boot.service;

import javax.mail.MessagingException;

public interface EmailService {


    public void sendEmailCode(String toEmail) throws MessagingException;


}
