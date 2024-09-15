package com.cg.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
@Entity
public class Customer {
	@Id
	@GeneratedValue
	private int accNo; // customerId
	private String customerName;
	private String address;
	private String phoneNo;
	private String email;
	private Double wallet;
	private String kyc_status;
	@NotNull
	private Boolean fileStatus; 
	
	

}
