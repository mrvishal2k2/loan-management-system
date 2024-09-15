package com.cg;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cg.api.CustomerController;
import com.cg.entity.Customer;
import com.cg.service.CustomerService;

public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    
 @Test
void testCreateCustomer() throws Exception {
    // Arrange
    Customer customer = new Customer();
    customer.setAccNo(102); 
    customer.setCustomerName("xxx");
    customer.setAddress("India");
    customer.setPhoneNo(null);
    customer.setEmail("john.doe@example.com");
    customer.setWallet(0.0); 
    customer.setKyc_status("Pending_Verification");
    customer.setFileStatus(true);

    when(customerService.saveCustomer(any(Customer.class))).thenReturn(customer);

    // Act
    mockMvc.perform(post("/api/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"accNo\":102,\"customerName\":\"xxx\",\"address\":\"India\",\"phoneNo\":null,\"email\":\"john.doe@example.com\",\"wallet\":0.0,\"kyc_status\":\"Pending_Verification\",\"fileStatus\":true}"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.accNo").value(102))
            .andExpect(jsonPath("$.customerName").value("xxx"))
            .andExpect(jsonPath("$.address").value("India"))
            .andExpect(jsonPath("$.phoneNo").isEmpty()) // Check for null
            .andExpect(jsonPath("$.email").value("john.doe@example.com"))
            .andExpect(jsonPath("$.wallet").value(0.0))
            .andExpect(jsonPath("$.kyc_status").value("Pending_Verification"))
            .andExpect(jsonPath("$.fileStatus").value(true));

    // Assert
    verify(customerService, times(1)).saveCustomer(any(Customer.class));
}


    @Test
    void testGetCustomer() throws Exception {
        Customer customer = new Customer(); 
        when(customerService.getCustomerById(1)).thenReturn(customer);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(customerService, times(1)).getCustomerById(1);
    }

    @Test
    void testUpdateKycStatus() throws Exception {
        Customer customer = new Customer(); 
        when(customerService.UpdateKycStatus(1)).thenReturn(customer);

        mockMvc.perform(put("/api/customers/update/kyc/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(customerService, times(1)).UpdateKycStatus(1);
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Customer updatedCustomer = new Customer(); // Populate with necessary fields
        when(customerService.UpdateCustomer(any(Customer.class), eq(1))).thenReturn(updatedCustomer);

        mockMvc.perform(put("/api/customers/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"field\":\"newValue\"}")) // Replace with actual JSON
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(customerService, times(1)).UpdateCustomer(any(Customer.class), eq(1));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        doNothing().when(customerService).deleteCustomer(1);

        mockMvc.perform(delete("/api/customers/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Customer Account Deleted"));

        verify(customerService, times(1)).deleteCustomer(1);
    }
}
