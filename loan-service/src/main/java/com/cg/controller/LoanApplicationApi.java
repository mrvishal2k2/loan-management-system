package com.cg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cg.entity.LoanApplication;
import com.cg.entity.LoanApplicationDTO;
import com.cg.service.LoanApplicationService;

@RestController
@RequestMapping("/loan/application")
public class LoanApplicationApi {
	
    @Autowired
    private LoanApplicationService loanApplicationService;

	// submit loan application
	@PostMapping
    public ResponseEntity<LoanApplication> submitApplication(@RequestBody LoanApplicationDTO application) {
        LoanApplication savedApplication = loanApplicationService.submitApplication(application);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedApplication);
    }

	// get loan application status by loanId
    @GetMapping("/{id}")
    public ResponseEntity<LoanApplication> getApplicationById(@PathVariable Long id) {
        return ResponseEntity.ok(loanApplicationService.getApplicationById(id));
    }
	// get loan application status by loanId
    @GetMapping("/user/{id}")
    public ResponseEntity<List<LoanApplication>> getApplicationsByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(loanApplicationService.getApplicationsByUserId(id));
    }
    
    // update loan application details by loanId	
    @PutMapping("/{id}")
    public ResponseEntity<LoanApplication> setApplicationById(@RequestBody LoanApplication loanApplication,@PathVariable Long id) {
        return ResponseEntity.ok(loanApplicationService.setApplicationById(loanApplication,id));
    }
    
    // list all loan applications
    @GetMapping
    public ResponseEntity<List<LoanApplication>> getAllApplications() {
        return ResponseEntity.ok(loanApplicationService.getAllApplications());
    }

    // update loan application Status as Approved or Rejected
    //  status?id=<id>
    @PutMapping("/status")
    public ResponseEntity<LoanApplication> updateApplicationStatus(@RequestParam Long id) {
        LoanApplication updatedApplication = loanApplicationService.updateApplicationStatus(id);
        return ResponseEntity.ok(updatedApplication);
    }
    // update loan application details by loanId	
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApplication( @PathVariable Long id) {
    	loanApplicationService.deleteApplicationById(id);
        return ResponseEntity.ok("Loan Application is Deleted");
    }
    
}
