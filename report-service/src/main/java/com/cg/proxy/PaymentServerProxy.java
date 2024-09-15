package com.cg.proxy;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cg.entity.PaymentDTO;

@FeignClient(name="payment-service")
public interface PaymentServerProxy {

	// get payment details by loanId
	@GetMapping("/api/payment/loan/{id}")
	List<PaymentDTO> getAllPayments(@PathVariable int id);

}
