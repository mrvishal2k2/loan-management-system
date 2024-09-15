package com.cg;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
 
import java.util.Collections;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
 
import com.cg.entity.CustomerDTO;
import com.cg.entity.LoanApplicationDTO;
import com.cg.entity.LoanReport;
import com.cg.entity.PaymentDTO;
import com.cg.proxy.CustomerServerProxy;
import com.cg.proxy.LoanServiceProxy;
import com.cg.proxy.PaymentServerProxy;
import com.cg.service.ReportService;
 
@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {
	@InjectMocks
    private ReportService reportService;
 
    @Mock
    private CustomerServerProxy cProxy;
 
    @Mock
    private LoanServiceProxy loanProxy;
 
    @Mock
    private PaymentServerProxy payProxy;
 
    private CustomerDTO customer;
    private LoanApplicationDTO loan;
    private PaymentDTO payment;
 
    @BeforeEach
    public void setUp() {
        // Initialize test data
        customer = new CustomerDTO();
        customer.setAccNo(1); // Assuming accNo is the identifier for customer
 
        loan = new LoanApplicationDTO();
        loan.setId(1);
        loan.setCustomerId(1); // Link loan to customer
 
        payment = new PaymentDTO(); // Sample payment data
        payment.setId(1);
    }
 
    @Test
    public void testGenerateReport_Success() {
        // Mocking behavior
        when(cProxy.getCustomer(anyLong())).thenReturn(customer);
        when(loanProxy.getAllApplications()).thenReturn(Collections.singletonList(loan));
        when(payProxy.getAllPayments(loan.getId())).thenReturn(Collections.singletonList(payment));
 
        // Generate report
        LoanReport report = reportService.generateReport(1L);
 
        // Assertions
        assertNotNull(report);
        assertEquals(customer, report.getCustomer());
        assertEquals(1, report.getLoans().size());
        assertEquals(1, report.getLoans().get(0).getPayments().size());
        assertEquals(payment, report.getLoans().get(0).getPayments().get(0));
    }
 
    @Test
    public void testGenerateReport_CustomerNotFound() {
        when(cProxy.getCustomer(anyLong())).thenReturn(null);
 
        LoanReport report = reportService.generateReport(1L);
 
        assertNotNull(report);
        assertNull(report.getCustomer());
        assertTrue(report.getLoans().isEmpty());
    }
 
    @Test
    public void testGenerateReport_NoLoans() {
        when(cProxy.getCustomer(anyLong())).thenReturn(customer);
        when(loanProxy.getAllApplications()).thenReturn(Collections.emptyList());
 
        LoanReport report = reportService.generateReport(1L);
 
        assertNotNull(report);
        assertEquals(customer, report.getCustomer());
        assertTrue(report.getLoans().isEmpty());
    }
 
    @Test
    public void testGenerateReport_LoanWithoutPayments() {
        when(cProxy.getCustomer(anyLong())).thenReturn(customer);
        when(loanProxy.getAllApplications()).thenReturn(Collections.singletonList(loan));
        when(payProxy.getAllPayments(loan.getId())).thenReturn(Collections.emptyList());
 
        LoanReport report = reportService.generateReport(1L);
 
        assertNotNull(report);
        assertEquals(customer, report.getCustomer());
        assertEquals(1, report.getLoans().size());
        assertTrue(report.getLoans().get(0).getPayments().isEmpty());
    }
}