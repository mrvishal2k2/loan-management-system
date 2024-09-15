package com.cg.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.Repository.CustomerRepository;
import com.cg.entity.Customer;
import com.cg.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

@Service
public  class CustomerServiceImpl implements CustomerService{
	 
	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
	  
    @Autowired
    private CustomerRepository customerRepository;
    
    // saving the customer details
    public Customer saveCustomer(Customer customer) {
    	  customer.setWallet(0.0);
    	  
    	  if(customer.getFileStatus()== null) {
    		  throw new ResourceNotFoundException("File status is Null, please pass the file");
    	  }
    	  if(customer.getFileStatus())
    	  {
    		  customer.setKyc_status("Pending_Verification");
    	  }
    	  else
    	  {
    		  customer.setKyc_status("Not_Submitted");
    	  }
     	 logger.info("Customer Details Saved");
   	  
        return customerRepository.save(customer);
    }
    
    @Override
    // get customer details by account Number
    public Customer getCustomerById(Integer id) {
        logger.info("Fetching customer with ID: {}", id);
        return customerRepository.findById(id).orElseThrow(() -> {
            logger.error("Customer not found with ID: {}", id);
            return new ResourceNotFoundException("Customer not found");
        });
    }
        		
    
    @Override
    // updating customer details by account Number and Data
    public Customer UpdateCustomer(Customer customer,  Integer id) {
    	Optional<Customer> myCustomer = customerRepository.findById(id);
		if(myCustomer.isPresent()) {
			customer.setKyc_status(myCustomer.get().getKyc_status());
			if(customer.getWallet()>0.00) {
				customer.setWallet(customer.getWallet());
			}
			else {
			customer.setWallet(myCustomer.get().getWallet());
			}
			customer.setAccNo(id);
			logger.info("Customer updated with ID: {}", customer.getAccNo());
			return customerRepository.save(customer);
		}
        logger.error("Customer not found with ID: {}", id);
		throw new ResourceNotFoundException("Customer ID not found");
	}
    
    @Override
    // update kyc status based on fileStatus
    public Customer UpdateKycStatus(Integer id) {
		if(customerRepository.findById(id).isPresent()) {
			Customer cust = customerRepository.findById(id).get();
			if(cust.getFileStatus())
	    	  {
				
				cust.setKyc_status("Verified");
	    	  }
			else
			{
				cust.setKyc_status("Rejected");
			}
			logger.info("Customer Kyc updated :{}", id);
			return customerRepository.save(cust);
		}
        logger.error("Customer not found with ID: {}", id);
		throw new ResourceNotFoundException("Customer ID not found");
	}
    
    @Override
    // delete customer Account
	public void deleteCustomer(int id) {
		logger.info("Customer Deleted account: {}", id);
    	customerRepository.deleteById(id);
	}
	
	
    
}