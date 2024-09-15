package com.cg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.entity.Payment;

public interface LoanRepository extends JpaRepository<Payment, Integer> {

    Payment findById(int id);
    List<Payment> findByLoanApplicationId(int loanApplicationId);
}
