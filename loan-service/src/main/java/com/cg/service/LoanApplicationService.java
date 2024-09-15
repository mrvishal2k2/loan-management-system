package com.cg.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.entity.CustomerDTO;
import com.cg.entity.LoanApplication;
import com.cg.entity.LoanApplicationDTO;
import com.cg.entity.Loantype;
import com.cg.exception.LoanApplicationException;
import com.cg.proxy.CustomerServiceProxy;
import com.cg.repository.LoanApplicationRepository;
import com.cg.repository.LoantypeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LoanApplicationService {
	
	private static final Logger logger = LoggerFactory.getLogger(LoanApplicationService.class);
	
    @Autowired
    private LoanApplicationRepository repository;
    @Autowired
    private LoantypeRepository ltype;

    
	@Autowired
	private CustomerServiceProxy proxy;
	
	// submit application for loan
    public LoanApplication submitApplication(LoanApplicationDTO application) {
        LoanApplication loanApplication = new LoanApplication();
        BeanUtils.copyProperties(application, loanApplication);
        loanApplication.setStatus("PENDING"); // Default status
        loanApplication.setDue_amount(0.0);
        loanApplication.setApplicationDate(LocalDate.now());
        logger.info("Loan Application Submitted : {}", loanApplication.getId());
        return repository.save(loanApplication);
    }

    // get application details by applicationId
    public LoanApplication getApplicationById(Long id) {
    	logger.info("Getting application details for loan id {} ", id);
        return repository.findById(id)
                         .orElseThrow(() -> {
                         logger.error("Loan id: {} not found", id);
                         return new LoanApplicationException("Loan application not found");
                         });
    }

    // update application details by applicationId
   public LoanApplication setApplicationById(LoanApplication loanApplication,  Long id) {
	   Optional<LoanApplication> myloan = repository.findById(id);
		if(myloan.isPresent()) {
			
			loanApplication.setId(id.intValue());
		    if (loanApplication.getStatus() != null && !loanApplication.getStatus().equals("INACTIVE")) {
		        loanApplication.setStatus(myloan.get().getStatus());
		    }

	        if(loanApplication.getDue_amount()==null) {
	        	loanApplication.setDue_amount(myloan.get().getDue_amount());
	        }

	        loanApplication.setApplicationDate(myloan.get().getApplicationDate());
	        
	        logger.info("Loan Application Updated for Loan Id: {}", id);
			return repository.save(loanApplication);
		}
		logger.error("Loan Id {} not found", id);
		throw new LoanApplicationException("Loan Application not found");
	}
    
   // get list of all loan applications
    public List<LoanApplication> getAllApplications() {
    	logger.info("Getting all loan applications");
        return repository.findAll();
    }

    // update loan approval status to sent to approval (admin)
    public LoanApplication updateApplicationStatus(Long id) {
        LoanApplication application = repository.findById(id)
                .orElseThrow(() -> new LoanApplicationException("Loan Application not found"));
        
        CustomerDTO customer = proxy.getCustomer(application.getCustomerId())
        		.orElseThrow(()-> {
        		throw new LoanApplicationException("Customer Not found");
        		});
        		
        
        if (customer == null || !customer.getKyc_status().equalsIgnoreCase("Verified")) {
            logger.error("Loan Application rejected for pending KYC");
            throw new LoanApplicationException("KYC NOT FINISHED");
        }
        
        Loantype t = ltype.findById(application.getLoanType()).orElseThrow(()-> {
            logger.error("Failed to find loan type");
            throw new LoanApplicationException("Failed to update status: Loan Type not Found");
        }
        );
        
        if (application.getLoanType().equalsIgnoreCase(t.getLoantype())) {
            application.setStatus("SENT_TO_APPROVAL");
            
            logger.info("Updated the loan application status for LoanId: {} to {}", id,"SENT_TO_APPROVAL");
        } else {
            logger.error("Loan type mismatch: {} vs {}", application.getLoanType(), t.getLoantype());
        }

        LoanApplication savedApplication = repository.save(application);
        logger.info("Saved loan application: {}", savedApplication);
        
        return savedApplication; 
    }

	public List<LoanApplication> getApplicationsByUserId(Long id) {
		
		return repository.findAll().stream().filter(
				x-> x.getCustomerId().equals(id)).toList(); 
	}

	public String deleteApplicationById(Long id) {
		Optional<LoanApplication> userOptional = repository.findById(id);
		if (userOptional.isPresent()) {
		    repository.deleteById(id);
		    return "User  deleted successfully.";
		} else {
		    return "User  not found, deletion skipped.";
		}
	}

}

