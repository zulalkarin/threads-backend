package com.threadmanager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ThreadManagerServiceTest {

    @Mock
    private QueueService queueService;

    private ThreadManagerService threadManagerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        threadManagerService = new ThreadManagerService(queueService);
    }

    @Test
    void createThreads_ShouldCreateSpecifiedNumberOfThreads() {
        

        // Act
        threadManagerService.createThreads(2, 1);

        // Assert
        assertEquals(3, threadManagerService.getAllThreadsInfo().size());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        verify(queueService, times(2)).addMessage(anyString());
        verify(queueService, atLeastOnce()).getMessage();
    }

    @Test
    void updateThreadStatus_ShouldChangeThreadStatus() {
        // Arrange
        threadManagerService.createThreads(1, 0);
        Long threadId = threadManagerService.getAllThreadsInfo().get(0).getId();

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Act
        threadManagerService.updateThreadStatus(threadId, false);

        // Assert
        assertFalse(threadManagerService.getAllThreadsInfo().get(0).isActive());
        verify(queueService, atLeastOnce()).addMessage(any(String.class));
        verify(queueService, never()).getMessage();
    }

    @Test
    void deleteAllThreads_ShouldRemoveAllThreads() {
        // Arrange
        threadManagerService.createThreads(2, 2);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Act
        threadManagerService.deleteAllThreads();

        // Assert
        assertTrue(threadManagerService.getAllThreadsInfo().isEmpty());
        verify(queueService, atLeast(2)).getMessage();
        verify(queueService, atLeast(2)).addMessage(any(String.class));
    }
}
