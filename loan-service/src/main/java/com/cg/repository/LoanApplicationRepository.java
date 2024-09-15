package com.cg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.entity.LoanApplication;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

}

