package com.cg.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


import com.cg.entity.CustomerDTO;

@FeignClient(name="customer-service")
public interface CustomerServerProxy {

	@GetMapping("/api/customers/{id}")
	 CustomerDTO getCustomer(@PathVariable Long id);
	 @PutMapping("/api/customers/update/{id}")
	 CustomerDTO updateCustomer(@RequestBody CustomerDTO customer,@PathVariable int id);

}
