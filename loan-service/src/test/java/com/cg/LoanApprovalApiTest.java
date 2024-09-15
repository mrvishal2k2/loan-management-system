package com.cg;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cg.controller.LoanApprovalApi;
import com.cg.entity.LoanApproval;
import com.cg.service.LoanServiceInterface;

@WebMvcTest(LoanApprovalApi.class)
public class LoanApprovalApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanServiceInterface loanApprovalService;

    @InjectMocks
    private LoanApprovalApi loanApprovalApi;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(loanApprovalApi).build();
    }

    @Test
    public void testGetLoanApproval() throws Exception {
        LoanApproval loanApproval = new LoanApproval();
        when(loanApprovalService.saveApplicationbyId(1L)).thenReturn(loanApproval);

        mockMvc.perform(post("/loan/approval/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        verify(loanApprovalService, times(1)).saveApplicationbyId(1L);
    }

    @Test
    public void testGetLoanApproval_NotFound() throws Exception {
        when(loanApprovalService.saveApplicationbyId(1L)).thenReturn(null);

        mockMvc.perform(post("/loan/approval/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist());

        verify(loanApprovalService, times(1)).saveApplicationbyId(1L);
    }

    @Test
    public void testEvaluateLoanApproval() throws Exception {
        LoanApproval loanApproval = new LoanApproval();
        when(loanApprovalService.evaluateLoanApplication(1)).thenReturn(loanApproval);

        mockMvc.perform(put("/loan/approval/evaluate/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        verify(loanApprovalService, times(1)).evaluateLoanApplication(1);
    }

    @Test
    public void testEvaluateLoanApproval_NotFound() throws Exception {
        when(loanApprovalService.evaluateLoanApplication(1)).thenReturn(null);

        mockMvc.perform(put("/loan/approval/evaluate/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist());

        verify(loanApprovalService, times(1)).evaluateLoanApplication(1);
    }
}
