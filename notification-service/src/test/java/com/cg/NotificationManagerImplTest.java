package com.cg;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.cg.service.NotificationManagerImpl;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class NotificationManagerImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private NotificationManagerImpl notificationManager;

    private final String fromEmail = "test@example.com";

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        setFromEmailId(notificationManager, fromEmail);
    }

    private void setFromEmailId(NotificationManagerImpl notificationManager, String fromEmail) throws Exception {
        Field field = NotificationManagerImpl.class.getDeclaredField("fromEmailId");
        field.setAccessible(true);
        field.set(notificationManager, fromEmail);
    }

    @Test
    public void testSendEmail() {
        // Given
        String message = "Hello, this is a test email.";
        String recipient = "mygmail@gmail.com";

        // When
        notificationManager.sendEmail(message, recipient);

        // Then
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals(fromEmail, sentMessage.getFrom());
        assertEquals(recipient, sentMessage.getTo()[0]);
        assertEquals(message, sentMessage.getText());
        assertEquals("Notification: ", sentMessage.getSubject());
    }
}