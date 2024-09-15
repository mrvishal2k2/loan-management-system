package com.cg;

import com.cg.api.NotificationController;
import com.cg.entity.NotifyDto;
import com.cg.service.NotificationsManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NotificationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NotificationsManagementService manager;

    @InjectMocks
    private NotificationController notificationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build(); // Set up MockMvc
    }

    @Test
    public void testSendMessage() throws Exception {
        // Given
        NotifyDto dto = new NotifyDto();
        dto.setMsg("Hello, this is a test email.");
        dto.setEmail("recipient@example.com");

        // When & Then
        mockMvc.perform(post("/notify/send-msg")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"msg\":\"Hello, this is a test email.\",\"email\":\"recipient@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Email Sent successfully"));

        // Verify that the sendEmail method was called
        ArgumentCaptor<String> msgCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        verify(manager, times(1)).sendEmail(msgCaptor.capture(), emailCaptor.capture());

        // Assert the captured values
        assertEquals("Hello, this is a test email.", msgCaptor.getValue());
        assertEquals("recipient@example.com", emailCaptor.getValue());
    }
}