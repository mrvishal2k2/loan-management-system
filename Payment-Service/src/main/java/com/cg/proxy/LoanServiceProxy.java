package com.cg.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cg.entity.LoanApplicationDTO;

@FeignClient(name="loan-service")
public interface LoanServiceProxy {
	
	@GetMapping("/loan/application/{id}")
	LoanApplicationDTO getLoanApplication(@PathVariable int id);
	
	@PutMapping("/loan/application/{id}")
	 LoanApplicationDTO updateLoanApplication(@RequestBody LoanApplicationDTO loanApplication,@PathVariable int id);

}
