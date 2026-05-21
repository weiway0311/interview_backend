package com.howord.backend.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Value("${app.domain.frontend}")
	private String frontEndDomain;
	
	public void sendResetPasswordMail(String to, String token) {
        String link = frontEndDomain + "/#/reset?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("密碼重設連結");
        message.setText("請點擊以下連結來重設密碼：\n" + link);
        mailSender.send(message);
    }

}
