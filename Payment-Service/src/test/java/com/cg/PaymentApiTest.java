package com.cg;
 
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
 
import java.util.Arrays;
import java.util.List;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cg.api.PaymentApi;
import com.cg.entity.Payment;
import com.cg.service.PaymentServiceinterface;
import org.springframework.http.MediaType;
 
@ExtendWith(MockitoExtension.class)
public class PaymentApiTest {
	 @Mock
	    private PaymentServiceinterface paymentService;
 
	    @InjectMocks
	    private PaymentApi paymentApi;
 
	    @Autowired
	    private MockMvc mockMvc;
 
	    private Payment payment;
 
	    @BeforeEach
	    public void setUp() {
	        mockMvc = MockMvcBuilders.standaloneSetup(paymentApi).build();
 
	        payment = new Payment();
	        payment.setId(1);
	        payment.setLoanApplicationId(123);
	        payment.setAmount(100.0);
	        payment.setMethod("ECS");
	        payment.setStatus("DONE");
	    }
 
	    @Test
	    public void testAcceptPayment() throws Exception {
	        when(paymentService.acceptPayment(any(Payment.class))).thenReturn(payment);
 
	        mockMvc.perform(post("/api/payment")
	                .contentType(MediaType.APPLICATION_JSON) // Use MediaType.APPLICATION_JSON
	                .content("{\"loanApplicationId\":123,\"amount\":100.0,\"method\":\"ECS\"}"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.id").value(1))
	                .andExpect(jsonPath("$.loanApplicationId").value(123))
	                .andExpect(jsonPath("$.amount").value(100.0))
	                .andExpect(jsonPath("$.method").value("ECS"))
	                .andExpect(jsonPath("$.status").value("DONE"));
 
	        verify(paymentService).acceptPayment(any(Payment.class));
	    }
 
	    @Test
	    public void testGetPaymentsById() throws Exception {
	        when(paymentService.getPaymentsById(1)).thenReturn(payment);
 
	        mockMvc.perform(get("/api/payment/status/1"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.id").value(1))
	                .andExpect(jsonPath("$.loanApplicationId").value(123))
	                .andExpect(jsonPath("$.amount").value(100.0))
	                .andExpect(jsonPath("$.method").value("ECS"))
	                .andExpect(jsonPath("$.status").value("DONE"));
 
	        verify(paymentService).getPaymentsById(1);
	    }
 
	    @Test
	    public void testGetAllPayments() throws Exception {
	        List<Payment> payments = Arrays.asList(payment);
	        when(paymentService.getAllPayments()).thenReturn(payments);
 
	        mockMvc.perform(get("/api/payment"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$[0].id").value(1))
	                .andExpect(jsonPath("$[0].loanApplicationId").value(123))
	                .andExpect(jsonPath("$[0].amount").value(100.0))
	                .andExpect(jsonPath("$[0].method").value("ECS"))
	                .andExpect(jsonPath("$[0].status").value("DONE"));
 
	        verify(paymentService).getAllPayments();
	    }
 
	    @Test
	    public void testGetPaymentsByLoanApplicationId() throws Exception {
	        List<Payment> payments = Arrays.asList(payment);
	        when(paymentService.getPaymentsByLoanApplicationId(123)).thenReturn(payments);
 
	        mockMvc.perform(get("/api/payment/loan/123"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$[0].id").value(1))
	                .andExpect(jsonPath("$[0].loanApplicationId").value(123))
	                .andExpect(jsonPath("$[0].amount").value(100.0))
	                .andExpect(jsonPath("$[0].method").value("ECS"))
	                .andExpect(jsonPath("$[0].status").value("DONE"));
 
	        verify(paymentService).getPaymentsByLoanApplicationId(123);
	    }
 
}