package com.cg.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Payment {
	@Id
	@GeneratedValue
    private Integer id;
	
	@NotNull
    private int loanApplicationId;
	@NotNull
    private double amount;
    private String method; // ECS, cheque, cash
    private String status; // e.g., "DONE", "FAIL"
    private LocalDate paymentDate;
    
}
