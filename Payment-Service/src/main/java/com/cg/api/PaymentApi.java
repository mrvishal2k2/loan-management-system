package com.cg.api;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.entity.Payment;
import com.cg.service.PaymentServiceinterface;

@RestController
@RequestMapping("/api/payment")
public class PaymentApi {

    @Autowired
    PaymentServiceinterface paymentService;

    
    // accept payment from user
    @PostMapping
    public ResponseEntity<Payment> acceptPayment(@RequestBody Payment payment) {
        Payment disbursedPayment = paymentService.acceptPayment(payment);
        return ResponseEntity.ok(disbursedPayment);
    }

    // check payment status with payment id
    @GetMapping("/status/{id}") // paymentId
    public ResponseEntity<Payment> getPaymentsById(@PathVariable Integer id) {
        Payment payments = paymentService.getPaymentsById(id);
        return ResponseEntity.ok(payments);
    }

    // Retrieve all payments
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    // Retrieve payments by loanApplicationId
    @GetMapping("/loan/{loanApplicationId}")
    public ResponseEntity<List<Payment>> getPaymentsByLoanApplicationId(@PathVariable int loanApplicationId) {
        List<Payment> payments = paymentService.getPaymentsByLoanApplicationId(loanApplicationId);
        return ResponseEntity.ok(payments);
    }
}
