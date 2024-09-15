package com.cg.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
@Entity
public class LoanApproval {
@Id
@GeneratedValue
private int approvalId;
@NotNull
private Long loanId;
private String approvalStatus;

}
