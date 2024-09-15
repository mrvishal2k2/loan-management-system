package com.cg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.entity.LoanApproval;

public interface LoanApprovalRepository extends JpaRepository<LoanApproval, Integer> {
	Optional<LoanApproval>  findByLoanId(Long loanId);
}
