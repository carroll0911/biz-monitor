package com.carroll.biz.monitor.analyzer.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * 邮件发送工具
 *
 * @author: hehongbo
 * @date 2019/9/29
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
@Component
@Slf4j
public class EmailUtils {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;

    public boolean sendEmail(String[] to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail + "@timanetworks.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            log.warn("send notify email failed:", e);
            return false;
        }
        return true;
    }
}
