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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cg.entity.CustomerDTO;
import com.cg.entity.LoanApplication;
import com.cg.entity.LoanApplicationDTO;
import com.cg.entity.Loantype;
import com.cg.exception.LoanApplicationException;
import com.cg.proxy.CustomerServiceProxy;
import com.cg.repository.LoanApplicationRepository;
import com.cg.repository.LoantypeRepository;
import com.cg.service.LoanApplicationService;

public class LoanApplicationServiceTest {

    @Mock
    private LoanApplicationRepository repository;

    @Mock
    private LoantypeRepository ltype;

    @Mock
    private CustomerServiceProxy proxy;

    @InjectMocks
    private LoanApplicationService service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSubmitApplication() {
        LoanApplicationDTO applicationDTO = new LoanApplicationDTO();
        LoanApplication application = new LoanApplication();
        when(repository.save(any(LoanApplication.class))).thenReturn(application);

        LoanApplication result = service.submitApplication(applicationDTO);

        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
        assertEquals(0.0, result.getDue_amount());
        assertEquals(LocalDate.now(), result.getApplicationDate());
        verify(repository, times(1)).save(any(LoanApplication.class));
    }

    @Test
    public void testGetApplicationById() {
        LoanApplication application = new LoanApplication();
        when(repository.findById(1L)).thenReturn(Optional.of(application));

        LoanApplication result = service.getApplicationById(1L);

        assertNotNull(result);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    public void testGetApplicationById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LoanApplicationException.class, () -> service.getApplicationById(1L));
        verify(repository, times(1)).findById(1L);
    }

    @Test
    public void testSetApplicationById() {
        LoanApplication existingApplication = new LoanApplication();
        existingApplication.setStatus("PENDING");
        existingApplication.setDue_amount(100.0);
        existingApplication.setApplicationDate(LocalDate.now());

        LoanApplication updatedApplication = new LoanApplication();
        updatedApplication.setStatus("APPROVED");

        when(repository.findById(1L)).thenReturn(Optional.of(existingApplication));
        when(repository.save(any(LoanApplication.class))).thenReturn(updatedApplication);

        LoanApplication result = service.setApplicationById(updatedApplication, 1L);

        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
        assertEquals(100.0, result.getDue_amount());
        assertEquals(LocalDate.now(), result.getApplicationDate());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(LoanApplication.class));
    }

    @Test
    public void testSetApplicationById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        LoanApplication updatedApplication = new LoanApplication();
        assertThrows(LoanApplicationException.class, () -> service.setApplicationById(updatedApplication, 1L));
        verify(repository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllApplications() {
        List<LoanApplication> applications = Arrays.asList(new LoanApplication(), new LoanApplication());
        when(repository.findAll()).thenReturn(applications);

        List<LoanApplication> result = service.getAllApplications();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testUpdateApplicationStatus() {
        LoanApplication application = new LoanApplication();
        application.setCustomerId(1L);
        application.setLoanType("Home Loan");

        CustomerDTO customer = new CustomerDTO();
        customer.setKyc_status("Verified");

        Loantype loantype = new Loantype();
        loantype.setLoantype("Home Loan");

        when(repository.findById(1L)).thenReturn(Optional.of(application));
        when(proxy.getCustomer(1L)).thenReturn(Optional.of(customer));
        when(ltype.findById("Home Loan")).thenReturn(Optional.of(loantype));
        when(repository.save(any(LoanApplication.class))).thenReturn(application);

        LoanApplication result = service.updateApplicationStatus(1L);

        assertNotNull(result);
        assertEquals("SENT_TO_APPROVAL", result.getStatus());
        verify(repository, times(1)).findById(1L);
        verify(proxy, times(1)).getCustomer(1L);
        verify(ltype, times(1)).findById("Home Loan");
        verify(repository, times(1)).save(any(LoanApplication.class));
    }

    @Test
    public void testUpdateApplicationStatus_KycNotFinished() {
        LoanApplication application = new LoanApplication();
        application.setCustomerId(1L);
        application.setLoanType("Home Loan");

        CustomerDTO customer = new CustomerDTO();
        customer.setKyc_status("Pending");

        when(repository.findById(1L)).thenReturn(Optional.of(application));
        when(proxy.getCustomer(1L)).thenReturn(Optional.of(customer));

        assertThrows(LoanApplicationException.class, () -> service.updateApplicationStatus(1L));
        verify(repository, times(1)).findById(1L);
        verify(proxy, times(1)).getCustomer(1L);
    }

    @Test
    public void testGetApplicationsByUserId() {
        LoanApplication application1 = new LoanApplication();
        application1.setCustomerId(1L);
        LoanApplication application2 = new LoanApplication();
        application2.setCustomerId(1L);

        when(repository.findAll()).thenReturn(Arrays.asList(application1, application2));

        List<LoanApplication> result = service.getApplicationsByUserId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testDeleteApplicationById() {
        LoanApplication application = new LoanApplication();
        when(repository.findById(1L)).thenReturn(Optional.of(application));

        String result = service.deleteApplicationById(1L);

        assertEquals("User  deleted successfully.", result);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteApplicationById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        String result = service.deleteApplicationById(1L);

        assertEquals("User  not found, deletion skipped.", result);
        verify(repository, times(1)).findById(1L);
    }
}
