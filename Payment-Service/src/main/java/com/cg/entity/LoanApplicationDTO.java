package com.cg.entity;


import java.time.LocalDate;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;


@Data
public class LoanApplicationDTO {

    private int id;
    private String loanType;
    private Long customerId;
    private Double loanAmount;
    private Double due_amount;
    private int duration;
    private  int creditScore;
    private String status;
    private LocalDate applicationDate;
    

}
