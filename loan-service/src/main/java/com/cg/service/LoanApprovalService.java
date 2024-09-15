package com.cg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.entity.CustomerDTO;
import com.cg.entity.LoanApplication;
import com.cg.entity.LoanApproval;
import com.cg.entity.Loantype;
import com.cg.exception.LoanApplicationException;
import com.cg.exception.LoanApprovalException;
import com.cg.proxy.CustomerServiceProxy;
import com.cg.repository.LoanApplicationRepository;
import com.cg.repository.LoanApprovalRepository;
import com.cg.repository.LoantypeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public  class LoanApprovalService implements LoanServiceInterface{
	
	private static final Logger logger = LoggerFactory.getLogger(LoanApprovalService.class);
	
	@Autowired
	LoanApprovalRepository loanApprovalrepo;
	@Autowired
	LoanApplicationRepository loanApprepo;
	@Autowired
	private CustomerServiceProxy customerProxy;
	
    @Autowired
    private LoantypeRepository loanTypeRepo;
	
	@Override
	// set application details into approval travel
	public LoanApproval saveApplicationbyId (Long loanId)
	{
		LoanApproval loanApproval = loanApprovalrepo.findByLoanId(loanId).orElseGet(()->{
				
				LoanApproval loan = new LoanApproval();
				loan.setApprovalStatus("APPROVAL_PENDING");
				loan.setLoanId(loanId);
				
				logger.info("Loan Application status changed to ACTIVE");
				return loanApprovalrepo.save(loan);
		}
				);
		
			loanApproval.setApprovalStatus("APPROVAL_PENDING");
			logger.info("Loan Application details added to Loan Approval");
			return  loanApprovalrepo.save(loanApproval);
	}
	
	// get Approval Status by loandId
	public LoanApproval getLoanApplication(int loanId) {
		return loanApprovalrepo.findById(loanId).orElseThrow(()-> 
		new LoanApprovalException("Loan Id not found")
		);
	}
	
	// evaluate loan and change status based on business criteria
	public LoanApproval evaluateLoanApplication( int approvalId) {
	       
		 // get loan approval details
		 LoanApproval loan = loanApprovalrepo.findById(approvalId)
					.orElseThrow(()-> {
	                    logger.error("Approval id: {} not found", approvalId);
	                    return new LoanApprovalException("Loan approval ID not found");
					});       
	                    
		 // get loan details using approval id
		 LoanApplication loanApplication = loanApprepo.findById(loan.getLoanId())
					.orElseThrow(()-> {
	                    logger.error("Loan id: {} not found", loan.getLoanId());
	                    return new LoanApprovalException("Loan application not found");
					});      
		 if(!loan.getApprovalStatus().equals("APPROVAL_PENDING")) {
			 throw new LoanApprovalException("Please get the Approval to continue.....");
		 }
	                    
	       
		 
	        if (loanApplication.getLoanAmount() > 490000) {
	            loan.setApprovalStatus("PENDING");
	        }
	        else if(loanApplication.getCreditScore() < 600) {
	           loan.setApprovalStatus("Rejected");
	        }   
	       else {
	    		CustomerDTO customer = customerProxy.getCustomer(loanApplication.getCustomerId()).orElseThrow(
	    				()->{
	    					throw new LoanApprovalException("Customer not found");
	    				}
	    				);
	    		
	    		if (customer.getKyc_status() == null || !customer.getKyc_status().equalsIgnoreCase("Verified")) {
	    		    throw new LoanApprovalException("KYC NOT DONE");
	    		}
	    		
	    		// update loan due amount in loan application
	            Loantype loanType = loanTypeRepo.findById(loanApplication.getLoanType()).orElseThrow(()-> {
	                logger.error("Failed to find loan type");
	                throw new LoanApplicationException("Failed to update status: Loan Type not Found");
	            }
	            );

	            double amount = loanApplication.getLoanAmount();
	            amount = amount * loanType.getInterest() * loanApplication.getDuration() / 100;
	            double totalAmount = loanApplication.getLoanAmount() + amount;
	            loanApplication.setDue_amount(totalAmount);
	    		loanApplication.setStatus("ACTIVE");
	    		
	    		loanApprepo.save(loanApplication);
	    		customer.setWallet(loanApplication.getLoanAmount());
	    		customerProxy.updateCustomer(customer,loanApplication.getCustomerId().intValue());
	    		
	    		logger.info("Loan amount disbursed for {}", loanApplication.getId());
	    		
	    		loan.setApprovalStatus("DISBURSED");
	    		
	        }
 
			return loanApprovalrepo.save(loan);
	 }

}

