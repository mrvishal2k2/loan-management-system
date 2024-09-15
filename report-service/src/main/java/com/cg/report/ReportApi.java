package com.cg.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cg.entity.LoanReport;
import com.cg.service.ReportService;

@RestController
@RequestMapping("/report")
public class ReportApi {
	
	@Autowired
	private ReportService rService;

	// generate loanReport from customerId
    @GetMapping("/{customerId}")
    public LoanReport getLoanReport(@PathVariable long customerId) {
    
    	return rService.generateReport(customerId);


    }
}

