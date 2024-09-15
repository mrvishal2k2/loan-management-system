package com.cg;

import com.cg.controller.LoanTypeApi;
import com.cg.entity.Loantype;
import com.cg.service.LoantypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoanTypeApiTest {

    @InjectMocks
    private LoanTypeApi loanTypeApi;

    @Mock
    private LoantypeService loanService;

    @Mock
    private Loantype loantype;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test case for getAllApplications method
    @Test
    public void testGetAllApplications() {
        // Mocking the behavior of the service
        List<Loantype> loanTypes = new ArrayList<>();
        loanTypes.add(new Loantype());
        loanTypes.add(new Loantype());
        when(loanService.getAllTypes()).thenReturn(loanTypes);

        // Call the method
        ResponseEntity<List<Loantype>> response = loanTypeApi.getAllApplications();

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(loanService).getAllTypes();
    }

    // Test case for submitApplication method
    @Test
    public void testSubmitApplication() {
        // Mocking the behavior of the service
        when(loanService.savetype(loantype)).thenReturn(loantype);

        // Call the method
        ResponseEntity<Loantype> response = loanTypeApi.submitApplication(loantype);

        // Assertions
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(loantype, response.getBody());
        verify(loanService).savetype(loantype);
    }
}