package com.cg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.entity.Loantype;
import com.cg.service.LoantypeService;

@RestController
@RequestMapping("/loan/type")
public class LoanTypeApi {
	@Autowired
    private LoantypeService loanService;
	
	// get loan types available
    @GetMapping
    public ResponseEntity<List<Loantype>> getAllApplications() {
    	return ResponseEntity.ok(loanService.getAllTypes());
    }
    
    // add new loan type (admin)
    @PostMapping
    public ResponseEntity<Loantype> submitApplication(@RequestBody Loantype type) {
        Loantype savedApplication = loanService.savetype(type);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedApplication);
    }
}
