package com.cg.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class NotificationManagerImpl implements NotificationsManagementService {
	private static final Logger logger = LoggerFactory.getLogger(NotificationManagerImpl.class);
	
	
	@Autowired
	private JavaMailSender javaMailSender;
	@Value("${spring.mail.username}")
	private String fromEmailId; 
		
	public void sendEmail(String msg,String recipient) {
		logger.info("Sending email to {}, Message is {}", recipient, msg);
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(fromEmailId);
		simpleMailMessage.setTo(recipient);
		simpleMailMessage.setText(msg);
		simpleMailMessage.setSubject("Notification: ");
		javaMailSender.send(simpleMailMessage);
		
		logger.info("Email sent to {}", recipient);
	}
	
}
