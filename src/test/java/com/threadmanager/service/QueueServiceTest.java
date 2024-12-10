package com.threadmanager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QueueServiceTest {

    private QueueService queueService;

    @BeforeEach
    void setUp() {
        queueService = new QueueService();
    }

    @Test
    void addMessage_ShouldAddMessageToQueue() {
        // Act
        boolean result = queueService.addMessage("Test message");

        // Assert
        assertTrue(result);
        assertEquals(1, queueService.getQueueSize());
    }

    @Test
    void getMessage_ShouldReturnAndRemoveMessage() {
        // Arrange
        queueService.addMessage("Test message");

        // Act
        String message = queueService.getMessage();

        // Assert
        assertEquals("Test message", message);
        assertEquals(0, queueService.getQueueSize());
    }

    @Test
    void clearQueue_ShouldClearQueue() {
        // Arrange
        queueService.addMessage("Test message");

        // Act
        queueService.clearQueue();

        // Assert
        assertEquals(0, queueService.getQueueSize());
    }
}