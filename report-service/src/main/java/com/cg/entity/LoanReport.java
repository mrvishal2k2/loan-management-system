package com.cg.entity;

import java.util.List;

import lombok.Data;

@Data
public class LoanReport {
    private CustomerDTO customer;
    private List<LoanApplicationDTO> loans;
}