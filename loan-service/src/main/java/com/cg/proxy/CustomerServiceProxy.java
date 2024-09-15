package com.cg.proxy;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


import com.cg.entity.CustomerDTO;

@FeignClient(name="customer-service")
public interface CustomerServiceProxy {

	@GetMapping("/api/customers/{id}")
	 Optional<CustomerDTO> getCustomer(@PathVariable Long id);
	 @PutMapping("/api/customers/update/{id}")
	 CustomerDTO updateCustomer(@RequestBody CustomerDTO customer,@PathVariable int id);

}
