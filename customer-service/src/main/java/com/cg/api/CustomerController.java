package com.cg.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.entity.Customer;
import com.cg.service.CustomerService;
import com.cg.service.CustomerServiceImpl;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // creating the customer account
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.saveCustomer(customer);
        return ResponseEntity.ok(savedCustomer);
    }
    
    // get customer account details
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Integer id) {
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }
    
    // can be accessed only as admin 
    // verify and update kyc status
    @PutMapping("/update/kyc/{id}")
    public ResponseEntity<Customer> UpdateKycStatus(@PathVariable Integer id) {
        Customer updatecustomer = customerService.UpdateKycStatus(id);
        return ResponseEntity.ok(updatecustomer);
    }
    
    // update customer details
    @PutMapping("/update/{id}")
    public ResponseEntity<Customer> UpdateCustomer(@RequestBody Customer customer,@PathVariable Integer id) {
        Customer updatecustomer = customerService.UpdateCustomer(customer, id);
        return ResponseEntity.ok(updatecustomer);
    }
    
    // delete customer account
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Integer id) {
         customerService.deleteCustomer(id);
         return ResponseEntity.ok("Customer Account Deleted");
    }
}