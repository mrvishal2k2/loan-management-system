package com.cg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cg.entity.CustomerDTO;
import com.cg.entity.LoanApplication;
import com.cg.entity.LoanApproval;
import com.cg.entity.Loantype;
import com.cg.exception.LoanApplicationException;
import com.cg.exception.LoanApprovalException;
import com.cg.proxy.CustomerServiceProxy;
import com.cg.repository.LoanApplicationRepository;
import com.cg.repository.LoanApprovalRepository;
import com.cg.repository.LoantypeRepository;
import com.cg.service.LoanApprovalService;

public class LoanApprovalServiceTest {

    @Mock
    private LoanApprovalRepository loanApprovalrepo;

    @Mock
    private LoanApplicationRepository loanApprepo;

    @Mock
    private CustomerServiceProxy customerProxy;

    @Mock
    private LoantypeRepository loanTypeRepo;

    @InjectMocks
    private LoanApprovalService loanApprovalService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveApplicationById_NewApplication() {
        LoanApproval loanApproval = new LoanApproval();
        loanApproval.setLoanId(1L);
        loanApproval.setApprovalStatus("APPROVAL_PENDING");

        when(loanApprovalrepo.findByLoanId(1L)).thenReturn(Optional.empty());
        when(loanApprovalrepo.save(any(LoanApproval.class))).thenReturn(loanApproval);

        LoanApproval result = loanApprovalService.saveApplicationbyId(1L);

        assertNotNull(result);
        assertEquals("APPROVAL_PENDING", result.getApprovalStatus());
        verify(loanApprovalrepo, times(1)).findByLoanId(1L);
        verify(loanApprovalrepo, times(2)).save(any(LoanApproval.class));
    }

    @Test
    public void testSaveApplicationById_ExistingApplication() {
        LoanApproval loanApproval = new LoanApproval();
        loanApproval.setLoanId(1L);
        loanApproval.setApprovalStatus("APPROVAL_PENDING");

        when(loanApprovalrepo.findByLoanId(1L)).thenReturn(Optional.of(loanApproval));
        when(loanApprovalrepo.save(any(LoanApproval.class))).thenReturn(loanApproval);

        LoanApproval result = loanApprovalService.saveApplicationbyId(1L);

        assertNotNull(result);
        assertEquals("APPROVAL_PENDING", result.getApprovalStatus());
        verify(loanApprovalrepo, times(1)).findByLoanId(1L);
        verify(loanApprovalrepo, times(1)).save(any(LoanApproval.class));
    }

    @Test
    public void testGetLoanApplication() {
        LoanApproval loanApproval = new LoanApproval();
        when(loanApprovalrepo.findById(1)).thenReturn(Optional.of(loanApproval));

        LoanApproval result = loanApprovalService.getLoanApplication(1);

        assertNotNull(result);
        verify(loanApprovalrepo, times(1)).findById(1);
    }

    @Test
    public void testGetLoanApplication_NotFound() {
        when(loanApprovalrepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(LoanApprovalException.class, () -> loanApprovalService.getLoanApplication(1));
        verify(loanApprovalrepo, times(1)).findById(1);
    }

    @Test
    public void testEvaluateLoanApplication() {
        LoanApproval loanApproval = new LoanApproval();
        loanApproval.setApprovalStatus("APPROVAL_PENDING");
        loanApproval.setLoanId(1L);

        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setLoanAmount(100000.0);
        loanApplication.setCreditScore(700);
        loanApplication.setCustomerId(1L);
        loanApplication.setLoanType("Home Loan");

        CustomerDTO customer = new CustomerDTO();
        customer.setKyc_status("Verified");

        Loantype loanType = new Loantype();
        loanType.setInterest(5.0f);

        when(loanApprovalrepo.findById(1)).thenReturn(Optional.of(loanApproval));
        when(loanApprepo.findById(1L)).thenReturn(Optional.of(loanApplication));
        when(customerProxy.getCustomer(1L)).thenReturn(Optional.of(customer));
        when(loanTypeRepo.findById("Home Loan")).thenReturn(Optional.of(loanType));
        when(loanApprovalrepo.save(any(LoanApproval.class))).thenReturn(loanApproval);
        when(loanApprepo.save(any(LoanApplication.class))).thenReturn(loanApplication);

        LoanApproval result = loanApprovalService.evaluateLoanApplication(1);

        assertNotNull(result);
        assertEquals("DISBURSED", result.getApprovalStatus());
        verify(loanApprovalrepo, times(1)).findById(1);
        verify(loanApprepo, times(1)).findById(1L);
        verify(customerProxy, times(1)).getCustomer(1L);
        verify(loanTypeRepo, times(1)).findById("Home Loan");
        verify(loanApprovalrepo, times(1)).save(any(LoanApproval.class));
        verify(loanApprepo, times(1)).save(any(LoanApplication.class));
    }

    @Test
    public void testEvaluateLoanApplication_LoanNotFound() {
        when(loanApprovalrepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(LoanApprovalException.class, () -> loanApprovalService.evaluateLoanApplication(1));
        verify(loanApprovalrepo, times(1)).findById(1);
    }

    @Test
    public void testEvaluateLoanApplication_LoanApplicationNotFound() {
        LoanApproval loanApproval = new LoanApproval();
        loanApproval.setApprovalStatus("APPROVAL_PENDING");
        loanApproval.setLoanId(1L);

        when(loanApprovalrepo.findById(1)).thenReturn(Optional.of(loanApproval));
        when(loanApprepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LoanApprovalException.class, () -> loanApprovalService.evaluateLoanApplication(1));
        verify(loanApprovalrepo, times(1)).findById(1);
        verify(loanApprepo, times(1)).findById(1L);
    }

    @Test
    public void testEvaluateLoanApplication_KycNotDone() {
        LoanApproval loanApproval = new LoanApproval();
        loanApproval.setApprovalStatus("APPROVAL_PENDING");
        loanApproval.setLoanId(1L);

        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setLoanAmount(100000.0);
        loanApplication.setCreditScore(700);
        loanApplication.setCustomerId(1L);
        loanApplication.setLoanType("Home Loan");

        CustomerDTO customer = new CustomerDTO();
        customer.setKyc_status("Pending");

        when(loanApprovalrepo.findById(1)).thenReturn(Optional.of(loanApproval));
        when(loanApprepo.findById(1L)).thenReturn(Optional.of(loanApplication));
        when(customerProxy.getCustomer(1L)).thenReturn(Optional.of(customer));

        assertThrows(LoanApprovalException.class, () -> loanApprovalService.evaluateLoanApplication(1));
        verify(loanApprovalrepo, times(1)).findById(1);
        verify(loanApprepo, times(1)).findById(1L);
        verify(customerProxy, times(1)).getCustomer(1L);
    }

    @Test
    public void testEvaluateLoanApplication_LoanTypeNotFound() {
        LoanApproval loanApproval = new LoanApproval();
        loanApproval.setApprovalStatus("APPROVAL_PENDING");
        loanApproval.setLoanId(1L);

        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setLoanAmount(100000.0);
        loanApplication.setCreditScore(700);
        loanApplication.setCustomerId(1L);
        loanApplication.setLoanType("Home Loan");

        CustomerDTO customer = new CustomerDTO();
        customer.setKyc_status("Verified");

        when(loanApprovalrepo.findById(1)).thenReturn(Optional.of(loanApproval));
        when(loanApprepo.findById(1L)).thenReturn(Optional.of(loanApplication));
        when(customerProxy.getCustomer(1L)).thenReturn(Optional.of(customer));
        when(loanTypeRepo.findById("Home Loan")).thenReturn(Optional.empty());

        assertThrows(LoanApplicationException.class, () -> loanApprovalService.evaluateLoanApplication(1));
        verify(loanApprovalrepo, times(1)).findById(1);
        verify(loanApprepo, times(1)).findById(1L);
        verify(customerProxy, times(1)).getCustomer(1L);
        verify(loanTypeRepo, times(1)).findById("Home Loan");
    }
}
