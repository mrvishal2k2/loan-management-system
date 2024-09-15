package com.cg.entity;

import java.time.LocalDate;

import lombok.Data;


@Data
public class PaymentDTO {

    private int id;
    private double amount;
    private String status; 
    private LocalDate paymentDate;
    
}
