package com.boot.service;

import javax.mail.MessagingException;

public interface emailService {


    public void sendEmailCode(String toEmail) throws MessagingException;


}
