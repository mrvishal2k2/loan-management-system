package com.cg;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
 
import com.cg.entity.LoanApplicationDTO;
import com.cg.entity.Payment;
import com.cg.exceptions.PaymentException;
import com.cg.proxy.LoanServiceProxy;
import com.cg.repository.LoanRepository;
import com.cg.service.PaymentService;
 
@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
	@InjectMocks
    private PaymentService paymentService;
 
    @Mock
    private LoanRepository loanRepository;
 
    @Mock
    private LoanServiceProxy loanServiceProxy;
 
    private Payment payment;
    private LoanApplicationDTO loanApplication;
 
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        payment = new Payment();
        payment.setLoanApplicationId(1);
        payment.setAmount(100.0);
        payment.setMethod("CASH");
 
        loanApplication = new LoanApplicationDTO();
        loanApplication.setId(1);
        loanApplication.setStatus("ACTIVE");
        loanApplication.setDue_amount(50.0);
    }
 
    @Test
    public void testAcceptPayment_Success() {
        // Arrange
        loanApplication.setStatus("ACTIVE"); // Ensure loan status is ACTIVE
        loanApplication.setDue_amount(100.0); // Set a positive due amount
        when(loanServiceProxy.getLoanApplication(payment.getLoanApplicationId())).thenReturn(loanApplication);
        // Force the payment status to "DONE"
        payment.setStatus("DONE"); // Set status explicitly
        when(loanRepository.save(any(Payment.class))).thenReturn(payment);
 
        // Act
        Payment result = paymentService.acceptPayment(payment);
 
        // Assert
        assertNotNull(result);
        assertEquals(payment.getLoanApplicationId(), result.getLoanApplicationId());
        assertEquals(LocalDate.now(), result.getPaymentDate());
        verify(loanServiceProxy).updateLoanApplication(loanApplication, payment.getLoanApplicationId());
        verify(loanRepository).save(payment);
    }
 
 
    
    @Test
    public void testAcceptPayment_LoanInactive() {
        // Arrange
        loanApplication.setStatus("INACTIVE");
        when(loanServiceProxy.getLoanApplication(payment.getLoanApplicationId())).thenReturn(loanApplication);
 
        // Act & Assert
        PaymentException exception = assertThrows(PaymentException.class, () -> {
            paymentService.acceptPayment(payment);
        });
        assertEquals("LOAN IS ALREADY INACTIVE", exception.getMessage());
    }

 
    @Test
    public void testGetAllPayments() {
        // Arrange
        when(loanRepository.findAll()).thenReturn(Arrays.asList(payment));
 
        // Act
        List<Payment> result = paymentService.getAllPayments();
 
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(payment.getLoanApplicationId(), result.get(0).getLoanApplicationId());
    }
 
    @Test
    public void testGetPaymentsByLoanApplicationId() {
        // Arrange
        when(loanRepository.findByLoanApplicationId(payment.getLoanApplicationId())).thenReturn(Arrays.asList(payment));
 
        // Act
        List<Payment> result = paymentService.getPaymentsByLoanApplicationId(payment.getLoanApplicationId());
 
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(payment.getLoanApplicationId(), result.get(0).getLoanApplicationId());
    }
}
 