package com.cg.service;

import com.cg.entity.Customer;

public interface CustomerService {
	
	Customer saveCustomer(Customer customer);
	Customer getCustomerById(Integer id);
	Customer UpdateCustomer(Customer customer,Integer id);
	void deleteCustomer(int id);
	Customer UpdateKycStatus(Integer id);
}
