package com.cg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.entity.LoanApproval;
import com.cg.service.LoanServiceInterface;

@RestController
@RequestMapping("/loan/approval")
public class LoanApprovalApi {
	@Autowired
	private LoanServiceInterface loanApprovalService;
	
	// to add loan application to loan approval(admin)
	@PostMapping("/{loanId}")
	public ResponseEntity<LoanApproval> getLoanApproval(@PathVariable Long loanId){
		LoanApproval loanApproval = loanApprovalService.saveApplicationbyId(loanId);
		return new ResponseEntity<>(loanApproval, HttpStatus.OK);
	}
	
	
	//to evaluate loan approval and add money(admin)
	@PutMapping("/evaluate/{approvalId}")
	public ResponseEntity<LoanApproval> evaluateLoanApproval( @PathVariable int approvalId){
		LoanApproval status = loanApprovalService.evaluateLoanApplication( approvalId);
		return new ResponseEntity<>(status, HttpStatus.OK);
	}
	

}
