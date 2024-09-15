package com.cg.entity;


import java.time.LocalDate;
import java.util.List;

import lombok.Data;


@Data
public class LoanApplicationDTO {

    private int id;
    private String loanType;
    private Double loanAmount;
    private int customerId;
    private int duration;
    private String status;
    private LocalDate applicationDate;
    private List<PaymentDTO> payments;
}
