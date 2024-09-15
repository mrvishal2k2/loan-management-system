package com.cg;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import com.cg.entity.LoanReport;
import com.cg.report.ReportApi;
import com.cg.service.ReportService;
 
@ExtendWith(MockitoExtension.class)
public class ReportApiTest {
	 @InjectMocks
	    private ReportApi reportApi;
 
	    @Mock
	    private ReportService reportService;
 
	    private LoanReport loanReport;
 
	    @BeforeEach
	    public void setUp() {
	        MockitoAnnotations.openMocks(this);
	        loanReport = new LoanReport();
	        // Initialize loanReport with necessary data
	    }
 
	    @Test
	    public void testGetLoanReport_Success() {
	        // Arrange
	        long customerId = 1L; // Example customer ID
	        when(reportService.generateReport(customerId)).thenReturn(loanReport);
 
	        // Act
	        LoanReport response = reportApi.getLoanReport(customerId);
 
	        // Assert
	        assertEquals(loanReport, response);
	    }
 
	    @Test
	    public void testGetLoanReport_CustomerNotFound() {
	        // Arrange
	        long customerId = 2L; // Example customer ID
	        when(reportService.generateReport(customerId)).thenThrow(new RuntimeException("Customer not found"));
 
	        // Act & Assert
	        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
	            reportApi.getLoanReport(customerId);
	        });
 
	        assertEquals("Customer not found", exception.getMessage());
	    }
}