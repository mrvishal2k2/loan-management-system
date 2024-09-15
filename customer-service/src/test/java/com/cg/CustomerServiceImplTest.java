package com.cg;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
 
import java.util.Optional;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import com.cg.Repository.CustomerRepository;
import com.cg.entity.Customer;
import com.cg.exceptions.ResourceNotFoundException;
import com.cg.service.CustomerServiceImpl;
 
@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {
 
    @Mock
    private CustomerRepository customerRepository;
 
    @InjectMocks
    private CustomerServiceImpl customerService;
 
    private Customer customer;
 
    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setAccNo(1);
        customer.setFileStatus(true);
        customer.setWallet(100.0);
    }
 
    @Test
    void testSaveCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
 
        Customer savedCustomer = customerService.saveCustomer(customer);
 
        assertNotNull(savedCustomer);
        assertEquals("Pending_Verification", savedCustomer.getKyc_status());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }
 
    @Test
    void testGetCustomerById() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
 
        Customer foundCustomer = customerService.getCustomerById(1);
 
        assertNotNull(foundCustomer);
        assertEquals(1, foundCustomer.getAccNo());
        verify(customerRepository, times(1)).findById(1);
    }
 
    @Test
    void testGetCustomerById_NotFound() {
        when(customerRepository.findById(1)).thenReturn(Optional.empty());
 
        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.getCustomerById(1);
        });
        verify(customerRepository, times(1)).findById(1);
    }
 
    @Test
    void testUpdateCustomer() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
 
        Customer updatedCustomer = customerService.UpdateCustomer(customer, 1);
 
        assertNotNull(updatedCustomer);
        assertEquals(1, updatedCustomer.getAccNo());
        verify(customerRepository, times(1)).findById(1);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }
 
    @Test
    void testUpdateKycStatus() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
 
        Customer updatedCustomer = customerService.UpdateKycStatus(1);
 
        assertNotNull(updatedCustomer);
        assertEquals("Verified", updatedCustomer.getKyc_status());
        verify(customerRepository, times(2)).findById(1);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }
 
    @Test
    void testDeleteCustomer() {
        doNothing().when(customerRepository).deleteById(1);
 
        customerService.deleteCustomer(1);
 
        verify(customerRepository, times(1)).deleteById(1);
    }
}