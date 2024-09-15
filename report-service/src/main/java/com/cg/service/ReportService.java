package com.cg.service;
import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cg.entity.CustomerDTO;
import com.cg.entity.LoanApplicationDTO;
import com.cg.entity.LoanReport;
import com.cg.entity.PaymentDTO;
import com.cg.proxy.CustomerServerProxy;
import com.cg.proxy.LoanServiceProxy;
import com.cg.proxy.PaymentServerProxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class ReportService {
	private static final Logger logger = LoggerFactory.getLogger(ReportService.class);
	
	@Autowired
	private CustomerServerProxy cProxy;
	@Autowired
	private LoanServiceProxy loanProxy;
	
	@Autowired
	private PaymentServerProxy payProxy;
	
	// generate loanReport
    public LoanReport generateReport(long customerId) {
    	logger.info("Generating Report for customer {}", customerId);
    	
    	LoanReport report = new LoanReport(); 
    	
    	CustomerDTO customer = cProxy.getCustomer(customerId);
    	report.setCustomer(customer);
    	
        List<LoanApplicationDTO> loans = loanProxy.getAllApplications().stream()
                .filter(x -> x.getCustomerId() == customer.getAccNo())
                .map(currentLoan -> {
                    List<PaymentDTO> payments = payProxy.getAllPayments(currentLoan.getId());
                    currentLoan.setPayments(payments);
                    return currentLoan; 
                })
                .collect(Collectors.toList()); 
        
            report.setLoans(loans); 
        	logger.info("Report Generated for customer {}", customerId);

		return report;
    }
}
