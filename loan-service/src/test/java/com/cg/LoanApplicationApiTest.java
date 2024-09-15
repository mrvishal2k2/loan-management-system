package com.cg;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cg.controller.LoanApplicationApi;
import com.cg.entity.LoanApplication;
import com.cg.entity.LoanApplicationDTO;
import com.cg.service.LoanApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(LoanApplicationApi.class)
public class LoanApplicationApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanApplicationService loanApplicationService;

    @InjectMocks
    private LoanApplicationApi loanApplicationApi;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(loanApplicationApi).build();
    }

    @Test
    public void testSubmitApplication() throws Exception {
        LoanApplicationDTO applicationDTO = new LoanApplicationDTO();
        LoanApplication application = new LoanApplication();
        when(loanApplicationService.submitApplication(applicationDTO)).thenReturn(application);

        mockMvc.perform(post("/loan/application")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(applicationDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        verify(loanApplicationService, times(1)).submitApplication(applicationDTO);
    }

    @Test
    public void testGetApplicationById() throws Exception {
        LoanApplication application = new LoanApplication();
        when(loanApplicationService.getApplicationById(1L)).thenReturn(application);

        mockMvc.perform(get("/loan/application/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        verify(loanApplicationService, times(1)).getApplicationById(1L);
    }

    @Test
    public void testGetApplicationsByUserId() throws Exception {
        List<LoanApplication> applications = Arrays.asList(new LoanApplication(), new LoanApplication());
        when(loanApplicationService.getApplicationsByUserId(1L)).thenReturn(applications);

        mockMvc.perform(get("/loan/application/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists());

        verify(loanApplicationService, times(1)).getApplicationsByUserId(1L);
    }

    @Test
    public void testSetApplicationById() throws Exception {
        LoanApplication application = new LoanApplication();
        when(loanApplicationService.setApplicationById(application, 1L)).thenReturn(application);

        mockMvc.perform(put("/loan/application/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(application)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        verify(loanApplicationService, times(1)).setApplicationById(application, 1L);
    }

    @Test
    public void testGetAllApplications() throws Exception {
        List<LoanApplication> applications = Arrays.asList(new LoanApplication(), new LoanApplication());
        when(loanApplicationService.getAllApplications()).thenReturn(applications);

        mockMvc.perform(get("/loan/application"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists());

        verify(loanApplicationService, times(1)).getAllApplications();
    }

    @Test
    public void testUpdateApplicationStatus() throws Exception {
        LoanApplication application = new LoanApplication();
        when(loanApplicationService.updateApplicationStatus(1L)).thenReturn(application);

        mockMvc.perform(put("/loan/application/status")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        verify(loanApplicationService, times(1)).updateApplicationStatus(1L);
    }

    @Test
    public void testDeleteApplication() throws Exception {
        mockMvc.perform(delete("/loan/application/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Loan Application is Deleted"));

        verify(loanApplicationService, times(1)).deleteApplicationById(1L);
    }
}
