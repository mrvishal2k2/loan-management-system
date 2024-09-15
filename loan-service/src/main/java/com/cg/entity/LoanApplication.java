package com.cg.entity;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class LoanApplication {
    @Id
    @GeneratedValue
    private int id;
    @NotNull
    private String loanType;
    @NotNull
    private Long customerId;
    @NotNull
    private Double loanAmount;
    private Double due_amount;
    @NotNull
    private int duration;
    @NotNull
    private  int creditScore;
    private String status; //  "PENDING", "APPROVED", "REJECTED"
    private LocalDate applicationDate;
    

}
