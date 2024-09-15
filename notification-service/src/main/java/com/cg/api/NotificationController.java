package com.cg.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.entity.NotifyDto;
import com.cg.service.NotificationsManagementService;

@RestController
@RequestMapping("/notify")
public class NotificationController {

	@Autowired
	private NotificationsManagementService manager;
	
	@GetMapping
	public String notifyme() {
		return "yes i am running";
	}
	
	@PostMapping("/send-msg")
	public String sendMessage(@RequestBody NotifyDto dto) {
		
		manager.sendEmail(dto.getMsg(),dto.getEmail());
		return "Email Sent successfully";
	}
	
}
