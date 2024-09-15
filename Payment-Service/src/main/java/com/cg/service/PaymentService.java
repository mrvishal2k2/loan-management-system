package com.cg.service;

import java.time.LocalDate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import com.cg.entity.LoanApplicationDTO;
import com.cg.entity.Payment;
import com.cg.exceptions.PaymentException;
import com.cg.exceptions.PaymentExceptionFail;
import com.cg.proxy.LoanServiceProxy;
import com.cg.repository.LoanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class PaymentService implements PaymentServiceinterface {
	private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
	

    @Autowired
    private LoanRepository loanRepository;
    
    @Autowired
    private LoanServiceProxy proxy;

    Random randomizer = new Random();
    
    @Override
    // accept payment from user
    public Payment acceptPayment(Payment payment) {
    	payment.setPaymentDate(LocalDate.now());
    	
    	// random payment status (usually fetched from payment api in real scenerio)
    	String[] options = {"DONE", "FAIL"};	
    	String randomItem = options[randomizer.nextInt(options.length)];
    	payment.setStatus(randomItem);
    	
    	
    	logger.info("Payment Status on Random: {}", randomItem);
    	LoanApplicationDTO loan = proxy.getLoanApplication(payment.getLoanApplicationId());

    	
    	if(loan.getStatus().equalsIgnoreCase("INACTIVE")) {
    		throw new PaymentException("LOAN IS ALREADY INACTIVE");
    	}
    	 // set to INACTIVE STATUS IF due LESS THAN 10
    	 if(randomItem.equalsIgnoreCase("DONE")) {
    		 
    		 loan.setDue_amount(loan.getDue_amount()-payment.getAmount());
    		 
    	    	if((loan.getDue_amount()-payment.getAmount()) <= 10) {
    	    		loan.setStatus("INACTIVE");
    	    		loan.setDue_amount(0.0);
    	    	}
    	    	
    		 proxy.updateLoanApplication(loan, payment.getLoanApplicationId());
    		 logger.info("Updated Loan Due: {}",loan.getId());
    	 }
    	 else {
    	throw new PaymentExceptionFail("Payment Failed");
    		 
    	 }
        return loanRepository.save(payment);
    }

    // get payment details by paymentId
    @Override
    public Payment getPaymentsById(Integer id) {
    	logger.info("Listing payment details by paymentId: {}", id);
    	
        return loanRepository.findById(id)
            .orElseThrow(() -> new PaymentException("Payment not found for id: " + id));
    }

    // get all payments
    @Override
    public List<Payment> getAllPayments() {
    	logger.info("Listing all Payment Records");
        return loanRepository.findAll();
    }

    // get payments done for specific loanApplication
    @Override
    public List<Payment> getPaymentsByLoanApplicationId(int loanApplicationId) {
    	logger.info("Listing payments done for specific loan application");
        return loanRepository.findByLoanApplicationId(loanApplicationId);
    }
}
