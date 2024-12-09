package com.threadmanager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.threadmanager.model.ThreadInfo;

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
        // Test öncesi mock nesnelerini başlat
        MockitoAnnotations.openMocks(this);
        threadManagerService = new ThreadManagerService(queueService);
    }

    @Test
    void createThreads_ShouldCreateSpecifiedNumberOfThreads() {
        // Arrange: Bu test için özel bir hazırlık gerekmiyor

        // Act: 2 sender ve 1 receiver thread oluştur
        threadManagerService.createThreads(2, 1);

        // Assert: Thread sayısını ve davranışlarını kontrol et
        assertEquals(3, threadManagerService.getAllThreadsInfo().size());

        // Thread'lerin başlaması için bekle
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // QueueService metodlarının doğru sayıda çağrıldığını doğrula
        verify(queueService, times(2)).addMessage(anyString());
        verify(queueService, atLeastOnce()).getMessage();
    }

    @Test
    void updateThreadStatus_ShouldChangeThreadStatus() {
        // Arrange: Bir sender thread oluştur ve ID'sini al
        threadManagerService.createThreads(1, 0);
        Long threadId = threadManagerService.getAllThreadsInfo().get(0).getId();

        // Thread'in başlaması için bekle
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Act: Thread'in durumunu değiştir
        threadManagerService.updateThreadStatus(threadId, false);

        // Assert: Thread'in durumunu ve davranışını kontrol et
        assertFalse(threadManagerService.getAllThreadsInfo().get(0).isActive());
        verify(queueService, atLeastOnce()).addMessage(any(String.class));
        verify(queueService, never()).getMessage();
    }

    @Test
    void deleteAllThreads_ShouldRemoveAllThreads() {
        // Arrange: İki sender ve iki receiver thread oluştur
        threadManagerService.createThreads(2, 2);

        // Thread'lerin başlaması için bekle
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Act: Tüm thread'leri sil
        threadManagerService.deleteAllThreads();

        // Assert: Thread'lerin silindiğini ve işlemlerini kontrol et
        assertTrue(threadManagerService.getAllThreadsInfo().isEmpty());
        verify(queueService, atLeast(2)).getMessage();
        verify(queueService, atLeast(2)).addMessage(any(String.class));
    }
}
