package com.cg.service;

import java.util.List;

import com.cg.entity.Payment;

public interface PaymentServiceinterface {

    Payment acceptPayment(Payment payment);

    List<Payment> getAllPayments();

    Payment getPaymentsById(Integer id);

    List<Payment> getPaymentsByLoanApplicationId(int loanApplicationId);
}
