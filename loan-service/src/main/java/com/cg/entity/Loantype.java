package com.cg.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Loantype {
	@Id
	private String loantype;
	private float interest;
	
	

}
