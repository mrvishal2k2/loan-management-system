package com.cg.service;

import com.cg.entity.LoanApproval;

public interface LoanServiceInterface {

	LoanApproval saveApplicationbyId(Long loanId);
	LoanApproval evaluateLoanApplication(int approvalId);
}
