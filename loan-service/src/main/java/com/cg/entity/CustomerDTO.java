package com.cg.entity;

import lombok.Data;

@Data

public class CustomerDTO {

	private int accNo;
	private String customerName;
	private String address;
	private String phoneNo;
	private String email;
	private Double wallet;
	private String kyc_status;
	private Boolean fileStatus; 
	
	

}
