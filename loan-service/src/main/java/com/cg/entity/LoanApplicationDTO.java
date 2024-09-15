package com.cg.entity;

import lombok.Data;


@Data
public class LoanApplicationDTO {
	

    private String loanType;
    private Long customerId;
    private Double loanAmount;
    private int duration;
    private  int creditScore;

}
