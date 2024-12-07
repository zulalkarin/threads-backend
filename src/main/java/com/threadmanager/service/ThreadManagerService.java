package com.threadmanager.service;

import com.threadmanager.model.ThreadInfo;
import com.threadmanager.thread.CustomThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.threadmanager.model.ThreadInfo.ThreadType;


@Service
public class ThreadManagerService {
    private final Map<Long, CustomThread> activeThreads = new ConcurrentHashMap<>();
    private final QueueService queueService;
    
    @Autowired
    public ThreadManagerService(QueueService queueService) {
        this.queueService = queueService;
    }
    
    public void createThreads(int senderCount, int receiverCount) {
        try {
            // create sender threads
            for (int i = 0; i < senderCount; i++) {
                CustomThread sender = new CustomThread(
                    "Sender-" + i,
                    ThreadType.SENDER,
                    queueService
                );
                activeThreads.put(sender.getId(), sender);
                sender.start();
                System.out.println("Sender thread created with ID: " + sender.getId());
            }
            
            // create receiver threads
            for (int i = 0; i < receiverCount; i++) {
                CustomThread receiver = new CustomThread(
                    "Receiver-" + i,
                    ThreadType.RECEIVER,
                    queueService
                );
                activeThreads.put(receiver.getId(), receiver);
                receiver.start();
            }
        } catch (Exception e) {
            System.out.println("Error creating threads: " + e);
            throw new RuntimeException("Thread creation failed", e);
        }
    }
    
    public void stopThread(Long threadId) {
        CustomThread thread = activeThreads.get(threadId);
        if (thread != null) {
            thread.stopThread();
            activeThreads.remove(threadId);
            System.out.println("Thread " + threadId + " stopped and removed from active threads");
        } else {
            System.out.println("Thread " + threadId + " not found");
        }
    }
    
    public void updateThreadPriority(Long threadId, int priority) {
        try {
            CustomThread thread = activeThreads.get(threadId);
            if (thread != null) {
                if (priority < Thread.MIN_PRIORITY || priority > Thread.MAX_PRIORITY) {
                    throw new IllegalArgumentException("Invalid priority value");
                }
                thread.setPriority(priority);
                System.out.println("Thread " + threadId + " priority updated to " + priority);
            } else {
                System.out.println("Thread " + threadId + " not found");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid priority value for thread " + threadId + ": " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("Error updating thread priority: " + e);
            throw new RuntimeException("Failed to update thread priority", e);
        }
    }
    
    public List<ThreadInfo> getAllThreadsInfo() {
        return activeThreads.values().stream()
            .map(CustomThread::getThreadInfo)
            .collect(Collectors.toList());
    }
}